package com.example.cardboardcompanion.domain.scheduled.manager

import android.content.Context
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.cardboardcompanion.domain.scheduled.worker.PriceUpdateWorker
import java.util.concurrent.TimeUnit

class PriceUpdateWorkManager (
    context: Context
) {

    private val workManager = WorkManager.getInstance(context)
    fun schedulePriceUpdates() {
        val scheduledWorkBuilder = PeriodicWorkRequestBuilder<PriceUpdateWorker>(
            repeatInterval = 1,
            repeatIntervalTimeUnit = TimeUnit.DAYS
        )

        workManager.enqueue(scheduledWorkBuilder.build())
    }

}