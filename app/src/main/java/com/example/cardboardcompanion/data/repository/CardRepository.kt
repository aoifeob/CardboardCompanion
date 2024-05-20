package com.example.cardboardcompanion.data.repository

import arrow.core.Either
import com.example.cardboardcompanion.data.source.CardValidationError
import com.example.cardboardcompanion.data.source.NetworkCardDataSource
import com.example.cardboardcompanion.model.card.ScryfallCard
import javax.inject.Inject

interface CardRepository {
    suspend fun findCard(
        set: String,
        collectorNo: String
    ): Either<CardValidationError, ScryfallCard?>
}

class NetworkCardRepository @Inject constructor(
    private val dataSource: NetworkCardDataSource
) : CardRepository {

    override suspend fun findCard(
        set: String,
        collectorNo: String
    ): Either<CardValidationError, ScryfallCard?> {
        return dataSource.validateCard(set, collectorNo)
    }

}