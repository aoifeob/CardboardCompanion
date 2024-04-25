package com.example.cardboardcompanion.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.cardboardcompanion.R
import com.example.cardboardcompanion.model.SortParam
import com.example.cardboardcompanion.model.card.Card
import com.example.cardboardcompanion.model.card.CardCollection
import com.example.cardboardcompanion.model.card.CardColour
import com.example.cardboardcompanion.model.filter.ColourFilter
import com.example.cardboardcompanion.model.filter.Filter
import com.example.cardboardcompanion.model.filter.PriceFilter
import com.example.cardboardcompanion.model.filter.SetFilter
import com.example.cardboardcompanion.ui.state.CollectionUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class CollectionViewModel : ViewModel() {

    private var cardCollection: CardCollection = getTestCardCollection()
    private val _uiState = MutableStateFlow(CollectionUiState(cardCollection))
    var visibleCards by mutableStateOf(_uiState.value.cardCollection.collection.sortedBy { it.name })
    val uiState: StateFlow<CollectionUiState> = _uiState.asStateFlow()
    internal var searchString by mutableStateOf("")
    internal var sortParam by mutableStateOf(SortParam.NAME_ASC)
    internal var filter: Filter? by mutableStateOf(null)

    fun updateCollection(
        updatedSearchString: String
    ) {
        searchString = updatedSearchString
        updateCollection(updatedSearchString, sortParam, filter)
    }

    fun updateCollection(
        updatedSortParam: SortParam
    ) {
        sortParam = updatedSortParam
        updateCollection(searchString, updatedSortParam, filter)
    }

    fun updateCollection(
        updatedFilter: Filter?
    ) {
        filter = updatedFilter
        updateCollection(searchString, sortParam, updatedFilter)
    }

    private fun updateCollection(
        searchString: String,
        sortParam: SortParam,
        filter: Filter?
    ) {
        visibleCards = if (searchString.isNotEmpty()) {
            cardCollection.collection.filter {
                it.name.contains(searchString, ignoreCase = true)
            }
        } else {
            cardCollection.collection
        }

        filter?.let { visibleCards = filterCollection(it) }

        visibleCards = sortCollection(sortParam)
    }

    private fun filterCollection(filter: Filter) : List<Card> {
        return when (filter) {
            is PriceFilter -> visibleCards.filter {
                filter.minPrice >= it.price && filter.maxPrice <= it.price
            }
            is SetFilter -> visibleCards.filter {
                filter.set == it.set
            }
            is ColourFilter -> visibleCards.filter {
                filter.colours.size == it.colours.size && filter.colours.containsAll(it.colours)
            }
            else -> cardCollection.collection
        }
    }

    private fun sortCollection(sortParam: SortParam) : List<Card> {
        return when (sortParam){
            SortParam.NAME_ASC -> visibleCards.sortedBy { it.name }
            SortParam.NAME_DESC -> visibleCards.sortedByDescending { it.name }
            SortParam.SET_ASC -> visibleCards.sortedBy { it.set }
            SortParam.SET_DESC -> visibleCards.sortedByDescending { it.set }
            SortParam.PRICE_ASC -> visibleCards.sortedBy { it.price }
            SortParam.PRICE_DESC -> visibleCards.sortedByDescending { it.price }
            else -> visibleCards.sortedBy { it.name }
        }
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
            ))
        )
    }

}