package com.example.cardboardcompanion.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cardboardcompanion.data.repository.CardRepository
import com.example.cardboardcompanion.data.repository.CollectionRepository
import com.example.cardboardcompanion.data.source.CardValidationError
import com.example.cardboardcompanion.model.card.Card
import com.example.cardboardcompanion.model.card.ScryfallCard
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class ScannerViewModel @Inject constructor(
    private val cardRepository: CardRepository,
    private val collectionRepository: CollectionRepository
) : ViewModel() {

    private val _cardCollection = MutableStateFlow(emptyList<Card>())

    private var detectedCardText: List<String> by mutableStateOf(emptyList())
    internal var shouldShowConfirmDialog: Boolean by mutableStateOf(false)
    internal var currentDetectedCard: ScryfallCard? by mutableStateOf(null)
    internal var cardValidationError: CardValidationError? by mutableStateOf(null)

    fun getOwnedCards() {
        viewModelScope.launch(Dispatchers.IO) {
            collectionRepository.getOwnedCards().collectLatest {
                _cardCollection.tryEmit(it)
            }
        }
    }

    fun updateDetectedCardText(detected: List<String>) {
        detectedCardText = detected
    }

    fun validateCard() {
        val cardDetails = extractCardDetails()
        if (cardDetails.first != null && cardDetails.second != null) {
            validateCard(cardDetails.first!!, cardDetails.second!!)
        }
    }

    private fun validateCard(set: String, collectorNo: String) {
        viewModelScope.launch {
            val detectedCardResult = cardRepository.findCard(set, collectorNo)

            if (detectedCardResult.isRight() && detectedCardResult.getOrNull() != null) {
                currentDetectedCard = detectedCardResult.getOrNull()
                if (currentDetectedCard != null) {
                    shouldShowConfirmDialog = true
                }
            } else if (detectedCardResult.isLeft() && detectedCardResult.leftOrNull() != null
            ) {
                cardValidationError = detectedCardResult.leftOrNull()
            }
        }
    }

    fun clearDetectedCardError() {
        cardValidationError = null
    }

    fun clearDetectedCard() {
        shouldShowConfirmDialog = false
        currentDetectedCard = null
    }

    fun addCardToCollection() {
        viewModelScope.launch {
            currentDetectedCard?.let { scryfallCard ->
                val mappedCard = scryfallCard.mapToCard()
                val matchingCard = _cardCollection.value.firstOrNull { mappedCard.isSameCard(it) }
                if (matchingCard != null)
                    collectionRepository.updateCardQuantity(matchingCard)
                else
                    collectionRepository.addCard(mappedCard)
            }
        }
        clearDetectedCard()
    }

    private fun extractCardDetails(): Pair<String?, String?> {
        val set = extractSet()

        val collectorNo = extractCollectorNo()

        return Pair(set, collectorNo)
    }

    private fun extractSet(): String? {
        val setTextBlock = detectedCardText.firstOrNull {
            it.contains(Regex("^[A-Z0-9]{3,5}(|.+EN)")) && !it.contains(Regex("^[0-9]{3}"))
        }
        if (setTextBlock != null) {
            return (Regex("^[A-Z0-9]{3,5}")).find(setTextBlock)?.value?.lowercase(Locale.ENGLISH)
        }
        return null
    }

    private fun extractCollectorNo(): String? {
        val collectorNoTextBlock = detectedCardText.firstOrNull {
            it.contains(Regex("\\d{3,4}|\\d{3}/\\d{3}")) && !it.contains(Regex("&"))
        }
        if (collectorNoTextBlock != null) {
            return collectorNoTextBlock.replace("[^0-9]".toRegex(), "")
                .replaceFirst("^0+(?!$)".toRegex(), "")
        }
        return null
    }

}