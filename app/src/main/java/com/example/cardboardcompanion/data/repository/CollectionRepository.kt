package com.example.cardboardcompanion.data.repository

import com.example.cardboardcompanion.model.SortParam
import com.example.cardboardcompanion.model.card.Card
import com.example.cardboardcompanion.model.card.CardDao
import com.example.cardboardcompanion.model.filter.Filter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface CollectionRepository {

    suspend fun getOwnedCards(): Flow<List<Card>>

    suspend fun getTopOwnedCardsByPrice(): Flow<List<Card>>

    suspend fun addCard(card: Card)

    suspend fun updateCard(card: Card)

    suspend fun updateCardQuantity(card: Card)

    suspend fun getOwnedCards(
        searchString: String,
        filter: Filter?,
        sortParam: SortParam
    ): Flow<List<Card>>

    suspend fun deleteCard(card: Card)
}

class LocalCollectionRepository @Inject constructor(
    private val dao: CardDao
) : CollectionRepository {

    override suspend fun getOwnedCards(): Flow<List<Card>> {
        return withContext(Dispatchers.IO) {
            dao.getOwnedCards()
        }
    }

    override suspend fun getTopOwnedCardsByPrice(): Flow<List<Card>> {
        return withContext(Dispatchers.IO) {
            dao.getTopOwnedCardsByPrice()
        }
    }

    override suspend fun addCard(card: Card) {
        withContext(Dispatchers.IO) {
            dao.addCard(card)
        }
    }

    override suspend fun updateCard(card: Card) {
        withContext(Dispatchers.IO) {
            dao.updateCard(card)
        }
    }

    override suspend fun updateCardQuantity(card: Card) {
        withContext(Dispatchers.IO) {
            val updatedCard = card.copy(
                id = card.id,
                name = card.name,
                set = card.set,
                collectorNo = card.collectorNo,
                image = card.image,
                price = card.price,
                quantity = card.quantity + 1
            )
            dao.updateCard(updatedCard)
        }
    }

    override suspend fun deleteCard(card: Card) {
        withContext(Dispatchers.IO) {
            dao.deleteCard(card)
        }
    }

    override suspend fun getOwnedCards(
        searchString: String,
        filter: Filter?,
        sortParam: SortParam
    ): Flow<List<Card>> {
        return withContext(Dispatchers.IO) {
            dao.getOwnedCards().map {
                it.applySearch(searchString).applyFilters(filter).applySort(sortParam)
            }
        }
    }

    private fun List<Card>.applySearch(
        searchString: String
    ): List<Card> {
        return filter {
            matchesSearchTerm(searchString, it)
        }
    }

    private fun matchesSearchTerm(searchString: String, card: Card): Boolean {
        return if (searchString.isNotBlank()) {
            return card.name.contains(searchString, ignoreCase = true)
        } else {
            true
        }
    }

    private fun List<Card>.applyFilters(
        filter: Filter?
    ): List<Card> {
        return filter {
            matchesFilters(filter, it)
        }
    }

    private fun matchesFilters(filter: Filter?, card: Card): Boolean {
        return if (filter != null) {
            if (filter.minPrice != null && card.price < filter.minPrice) {
                return false
            }
            if (filter.maxPrice != null && card.price > filter.maxPrice) {
                return false
            }
            return true
        } else {
            true
        }
    }

    private fun List<Card>.applySort(sortParam: SortParam): List<Card> {
        return when (sortParam) {
            SortParam.NAME_DESC -> sortedByDescending { it.name }
            SortParam.SET_ASC -> sortedBy { it.set }
            SortParam.SET_DESC -> sortedByDescending { it.set }
            SortParam.PRICE_ASC -> sortedBy { it.price }
            SortParam.PRICE_DESC -> sortedByDescending { it.price }
            else -> sortedBy { it.name }
        }
    }

}