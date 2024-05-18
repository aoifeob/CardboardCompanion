package com.example.cardboardcompanion.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cardboardcompanion.data.repository.CardRepository
import com.example.cardboardcompanion.model.card.Card
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InsightsViewModel @Inject constructor(
    private val repository: CardRepository
) : ViewModel() {

    private var _topCardCollection = MutableStateFlow(emptyList<Card>())
    val topCardCollection = _topCardCollection.asStateFlow()

    private var _collectionValue = MutableStateFlow("€0.00")
    val collectionValue = _collectionValue.asStateFlow()

    private var _isCollectionEmpty = MutableStateFlow(false)
    val isCollectionEmpty = _isCollectionEmpty.asStateFlow()

    fun getInsights() {
        viewModelScope.launch {
            repository.getTopOwnedCardsByPrice().collectLatest { cardList ->
                _topCardCollection.tryEmit(cardList.take(5))
                _collectionValue.tryEmit(calculateCollectionValue(cardList.sumOf { it.price * it.quantity }))
                _isCollectionEmpty.tryEmit(cardList.isEmpty())
            }
        }
    }

    private fun calculateCollectionValue(total: Double): String {
        return "€%.${2}f".format(total)
    }

}
