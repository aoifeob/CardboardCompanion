package com.example.cardboardcompanion.data.source

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.example.cardboardcompanion.data.sevice.ScryfallApiService
import com.example.cardboardcompanion.model.card.ScryfallCard
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class NetworkCardDataSource @Inject constructor(
    private val api: ScryfallApiService
) {

    suspend fun validateCard(
        set: String,
        collectorNo: String
    ): Either<CardValidationError, ScryfallCard?> {
        try {
            val response = api.getCard(set, collectorNo)
            if (!response.isSuccessful || response.body() == null) {
                return CardValidationError.CardNotFoundError.left()
            }
            return response.body().right()
        } catch (e: IOException) {
            return CardValidationError.NetworkError.left()
        } catch (e: HttpException) {
            return CardValidationError.NetworkError.left()
        }
    }

}

sealed interface CardValidationError {
    data object CardNotFoundError : CardValidationError
    data object NetworkError : CardValidationError
}