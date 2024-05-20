package com.example.cardboardcompanion.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cardboardcompanion.data.repository.CardRepository
import com.example.cardboardcompanion.data.repository.CollectionRepository
import com.example.cardboardcompanion.data.source.CardValidationError
import com.example.cardboardcompanion.model.card.DetectedCard
import com.example.cardboardcompanion.model.card.ScryfallCard
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class ScannerViewModel @Inject constructor(
    private val cardRepository: CardRepository,
    private val collectionRepository: CollectionRepository
) : ViewModel() {

    private var detectedCardText: List<String> by mutableStateOf(emptyList())
    internal var shouldShowConfirmDialog: Boolean by mutableStateOf(false)
    internal var currentDetectedCard: ScryfallCard? by mutableStateOf(null)
    internal var cardValidationError: CardValidationError? by mutableStateOf(null)

    fun updateDetectedCardText(detected: List<String>) {
        detectedCardText = detected
    }

    fun validateCard() {
        val cardDetails = extractCardDetails()
        if (cardDetails.first != null && cardDetails.second != null) {
            val detectedCard = DetectedCard(cardDetails.first!!, cardDetails.second!!)
            validateCard(detectedCard)
        }
    }

    private fun validateCard(card: DetectedCard) {
        viewModelScope.launch {
            val detectedCardResult = cardRepository.findCard(card.set, card.collectorNo)

            if (detectedCardResult.isRight() && detectedCardResult.getOrNull() != null) {
                currentDetectedCard = detectedCardResult.getOrNull()
                if (currentDetectedCard != null) {
                    shouldShowConfirmDialog = true;
                }
            } else if (detectedCardResult.isLeft() && detectedCardResult.leftOrNull() != null
            ) {
                cardValidationError = detectedCardResult.leftOrNull()
            }
        }
    }

    fun clearDetectedCardError(){
        cardValidationError = null
    }

    fun clearDetectedCard(){
        shouldShowConfirmDialog = false
        currentDetectedCard = null
    }

    fun addCardToCollection() {
        viewModelScope.launch {
            currentDetectedCard?.let { collectionRepository.addCard(it.mapToCard())}
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
            it.contains(Regex("^[A-Z]{3,5}(|.+EN)"))
        }
        if (setTextBlock != null) {
            return (Regex("^[A-Z]{3,5}")).find(setTextBlock)?.value?.lowercase(Locale.ENGLISH)
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