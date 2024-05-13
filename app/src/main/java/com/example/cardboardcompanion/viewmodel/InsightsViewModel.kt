package com.example.cardboardcompanion.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.cardboardcompanion.R
import com.example.cardboardcompanion.model.card.Card
import com.example.cardboardcompanion.model.card.CardCollection
import com.example.cardboardcompanion.model.card.CardColour
import com.example.cardboardcompanion.ui.state.InsightsUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class InsightsViewModel : ViewModel() {
    private var cardCollection: CardCollection = getTestCardCollection()
    private var collectionValue by mutableStateOf(calculateCollectionValue())
    private var mostExpensiveCardsCollection = getMostExpensiveCards()
    private val _uiState = MutableStateFlow(InsightsUiState(collectionValue, mostExpensiveCardsCollection))

    val uiState: StateFlow<InsightsUiState> = _uiState.asStateFlow()

    private fun calculateCollectionValue(): String {
        val totalValue = cardCollection.collection.sumOf { it.price * it.quantity }
        return "€%.${2}f".format(totalValue)
    }

    private fun getMostExpensiveCards(): List<Card> {
        return cardCollection.collection.sortedByDescending { it.price }.take(5)
    }

    //TODO: remove test data
    private fun getTestCardCollection(): CardCollection {
        return CardCollection(
            listOf(
                Card(
                    1,
                    "Lightning Bolt",
                    "2X2",
                    117,
                    R.drawable.card_lightning_bolt_2x2_117,
                    2.30,
                    4,
                    listOf(CardColour.RED)
                ),
                Card(
                    1,
                    "Lightning Bolt",
                    "CLB",
                    187,
                    R.drawable.card_lightning_bolt_clb_187,
                    1.18,
                    2,
                    listOf(CardColour.RED)
                ),
                Card(
                    1,
                    "Humility",
                    "TPR",
                    16,
                    R.drawable.card_humility_tpr_16,
                    36.76,
                    1,
                    listOf(CardColour.WHITE)
                ),
                Card(
                    1,
                    "Horizon Canopy",
                    "IMA",
                    240,
                    R.drawable.card_horizon_canopy_ima_240,
                    5.25,
                    4,
                    listOf(CardColour.GREEN, CardColour.WHITE)
                ),
                Card(
                    1,
                    "Thalia's Lancers",
                    "EMN",
                    47,
                    R.drawable.card_thalia_s_lancers_emn_47,
                    0.45,
                    3,
                    listOf(CardColour.WHITE)
                )
            )
        )
    }

}
