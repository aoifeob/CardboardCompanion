package com.example.cardboardcompanion.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cardboardcompanion.data.repository.CollectionRepository
import com.example.cardboardcompanion.model.SortParam
import com.example.cardboardcompanion.model.card.Card
import com.example.cardboardcompanion.model.filter.Filter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CollectionViewModel @Inject constructor(
    private val repository: CollectionRepository
) : ViewModel() {

    private val _cardCollection = MutableStateFlow(emptyList<Card>())
    val cardCollection = _cardCollection.asStateFlow()

    private val _isCollectionEmpty = MutableStateFlow(false)
    val isCollectionEmpty = _isCollectionEmpty.asStateFlow()

    internal var isActiveSearch by mutableStateOf(false)
    private var currentSearchString by mutableStateOf("")
    internal var searchParam by mutableStateOf("")
    internal var sortParam by mutableStateOf(SortParam.NAME_ASC)
    internal var filter: Filter? by mutableStateOf(null)

    private fun clearCustomisations(){
        isActiveSearch = false
        currentSearchString = ""
        searchParam = ""
        sortParam = SortParam.NAME_ASC
        filter = null
    }

    fun getOwnedCards() {
        viewModelScope.launch(IO) {
            clearCustomisations()
            repository.getOwnedCards().collectLatest {
                _cardCollection.tryEmit(it)
                _isCollectionEmpty.tryEmit(it.isEmpty())
            }
        }
    }

    private fun updateOwnedCards(
        searchParam: String,
        sortParam: SortParam,
        filter: Filter?
    ) {
        viewModelScope.launch(IO) {
            repository.getOwnedCards(searchParam, filter, sortParam).collectLatest {
                _cardCollection.tryEmit(it)
            }
        }
    }

    fun onSearchParamUpdated(updatedSearchString: String) {
        searchParam = updatedSearchString
    }

    fun onSearchExecuted(searchString: String) {
        if (currentSearchString != searchString) {
            currentSearchString = searchString
            isActiveSearch = true
            updateOwnedCards(searchParam, sortParam, filter)
        }
    }

    fun onSortExecuted(
        updatedSortParam: SortParam
    ) {
        if (sortParam != updatedSortParam) {
            sortParam = updatedSortParam
            updateOwnedCards(searchParam, updatedSortParam, filter)
        }
    }

    fun onFilterExecuted(
        updatedFilter: Filter?
    ) {
        if (filter != updatedFilter) {
            filter = updatedFilter
            updateOwnedCards(currentSearchString, sortParam, filter)
        }
    }

}