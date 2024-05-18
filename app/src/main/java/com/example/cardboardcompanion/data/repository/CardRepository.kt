package com.example.cardboardcompanion.data.repository

import com.example.cardboardcompanion.model.card.Card
import com.example.cardboardcompanion.model.card.CardDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CardRepository @Inject constructor(private val dao: CardDao) {
    suspend fun getCards(): Flow<List<Card>> {
        return withContext(Dispatchers.IO) {
            dao.getCards()
        }
    }

    suspend fun getOwnedCards(): Flow<List<Card>> {
        return withContext(Dispatchers.IO) {
            dao.getOwnedCards()
        }
    }
}