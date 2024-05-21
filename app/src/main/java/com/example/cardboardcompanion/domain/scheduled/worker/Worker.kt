package com.example.cardboardcompanion.domain.scheduled.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.cardboardcompanion.data.repository.CardRepository
import com.example.cardboardcompanion.data.repository.CollectionRepository
import com.example.cardboardcompanion.model.card.Card
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

@HiltWorker
class PriceUpdateWorker @AssistedInject constructor(
    @Assisted ctx: Context,
    @Assisted params: WorkerParameters,
    private val collectionRepository: CollectionRepository,
    private val cardRepository: CardRepository
) : CoroutineWorker(ctx, params) {

    override suspend fun doWork(): Result {
        withContext(Dispatchers.IO) {
            collectionRepository.getOwnedCards().map { cardList ->
                updateCollection(cardList)
            }
        }

        return Result.success()
    }

    private suspend fun updateCollection(cardList: List<Card>) {
        cardList.map {
            updateCard(it)
        }
    }

    private suspend fun updateCard(it: Card) {
        val currentCardData = cardRepository.findCard(it.set, it.collectorNo)
        if (currentCardData.isRight() &&
            currentCardData.getOrNull() != null &&
            it.price != currentCardData.getOrNull()!!.price.eurPrice
        ) {
            val updatedCard = it.copy(
                id = it.id,
                name = it.name,
                set = it.set,
                collectorNo = it.collectorNo,
                image = it.image,
                price = currentCardData.getOrNull()!!.price.eurPrice,
                quantity = it.quantity
            )

            collectionRepository.updateCard(updatedCard)
        }
    }

}