package com.example.cardboardcompanion.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.cardboardcompanion.R
import com.example.cardboardcompanion.model.SortParam
import com.example.cardboardcompanion.model.card.Card
import com.example.cardboardcompanion.model.card.CardCollection
import com.example.cardboardcompanion.model.filter.Filter
import com.example.cardboardcompanion.ui.state.CollectionUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.math.roundToInt

class CollectionViewModel : ViewModel() {

    private var cardCollection: CardCollection = getTestCardCollection()
    private val _uiState = MutableStateFlow(CollectionUiState(cardCollection))
    var visibleCards by mutableStateOf(_uiState.value.cardCollection.collection.sortedBy { it.name })
    val uiState: StateFlow<CollectionUiState> = _uiState.asStateFlow()
    internal var isActiveSearch by mutableStateOf(false)
    private var currentSearchString by mutableStateOf("")
    internal var searchParam by mutableStateOf("")
    internal var sortParam by mutableStateOf(SortParam.NAME_ASC)
    internal var filter: Filter? by mutableStateOf(null)
    internal var minPriceRestriction = 0f
    internal var maxPriceRestriction = cardCollection.collection.maxBy { it.price }.price.roundToInt().toFloat()

    fun onSearchParamUpdated(updatedSearchString: String) {
        searchParam = updatedSearchString
    }

    fun onSearchExecuted(searchString: String) {
        if (currentSearchString != searchString) {
            currentSearchString = searchString
            updateCollection(searchParam, sortParam, filter)
        }
    }

    fun onSortExecuted(
        updatedSortParam: SortParam
    ) {
        if (sortParam != updatedSortParam) {
            sortParam = updatedSortParam
            updateCollection(searchParam, updatedSortParam, filter)
        }
    }

    fun onFilterExecuted(
        updatedFilter: Filter?
    ) {
        if (filter != updatedFilter) {
            filter = updatedFilter
            updateCollection(currentSearchString, sortParam, filter)
        }
    }

    private fun updateCollection(
        searchString: String,
        sortParam: SortParam,
        filter: Filter?
    ) {
        searchCollection(searchString)
        filterCollection(filter)
        sortCollection(sortParam)
    }

    private fun searchCollection(searchString: String) {
        visibleCards = if (searchString.isNotBlank()) {
            isActiveSearch = true
            cardCollection.collection.filter {
                it.name.contains(searchString, ignoreCase = true)
            }
        } else {
            isActiveSearch = false
            cardCollection.collection
        }
    }

    private fun filterCollection(filter: Filter?) {
        if (filter != null) {
            filter.minPrice?.let { minPrice ->
                visibleCards = visibleCards.filter { minPrice <= it.price }
            }
            filter.maxPrice?.let { maxPrice ->
                visibleCards = visibleCards.filter { maxPrice >= it.price }
            }
        }
    }

    private fun sortCollection(sortParam: SortParam) {
        visibleCards = when (sortParam) {
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
                    4),
                Card(
                    1,
                    "Lightning Bolt",
                    "CLB",
                    187,
                    R.drawable.card_lightning_bolt_clb_187,
                    1.18,
                    2),
                Card(
                    1,
                    "Humility",
                    "TPR",
                    16,
                    R.drawable.card_humility_tpr_16,
                    36.76,
                    1),
                Card(
                    1,
                    "Horizon Canopy",
                    "IMA",
                    240,
                    R.drawable.card_horizon_canopy_ima_240,
                    5.25,
                    4),
                Card(
                    1,
                    "Thalia's Lancers",
                    "EMN",
                    47,
                    R.drawable.card_thalia_s_lancers_emn_47,
                    0.45,
                    3)
            )
        )
    }

}
