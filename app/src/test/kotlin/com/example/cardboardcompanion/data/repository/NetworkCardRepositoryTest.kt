package com.example.cardboardcompanion.data.repository

import arrow.core.left
import arrow.core.right
import com.example.cardboardcompanion.data.source.CardValidationError
import com.example.cardboardcompanion.data.source.NetworkCardDataSource
import com.example.cardboardcompanion.fixture.testCollectorNo
import com.example.cardboardcompanion.fixture.testScryfallCard
import com.example.cardboardcompanion.fixture.testSet
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class NetworkCardRepositoryTest {

    private val mockDataSource = mockk<NetworkCardDataSource>()
    private val repository = NetworkCardRepository(mockDataSource)

    @Test
    fun cardRepository_findCard_shouldReturnMatchingCard_whenSetAndCollectorNoAreValid() = runTest {
        val expectedResult = testScryfallCard
        val set = testSet
        val collectorNo = testCollectorNo
        coEvery { mockDataSource.validateCard(set, collectorNo) } returns testScryfallCard.right()

        val actualResult = repository.findCard(set, collectorNo)

        assertNotNull(actualResult.getOrNull())
        assertEquals(expectedResult, actualResult.getOrNull())
    }

    @Test
    fun cardRepository_findCard_shouldReturnCardNotFoundError_whenNoMatchingCardFound() = runTest {
        val expectedResult = CardValidationError.CardNotFoundError
        val set = testSet
        val collectorNo = testCollectorNo
        coEvery { mockDataSource.validateCard(set, collectorNo) } returns CardValidationError.CardNotFoundError.left()

        val actualResult = repository.findCard(set, collectorNo)

        assertNotNull(actualResult.leftOrNull())
        assertEquals(expectedResult, actualResult.leftOrNull())
    }

    @Test
    fun cardRepository_findCard_shouldReturnNetworkError_whenNetworkErrorOccurred() = runTest {
        val expectedResult = CardValidationError.NetworkError
        val set = testSet
        val collectorNo = testCollectorNo
        coEvery { mockDataSource.validateCard(set, collectorNo) } returns CardValidationError.NetworkError.left()

        val actualResult = repository.findCard(set, collectorNo)

        assertNotNull(actualResult.leftOrNull())
        assertEquals(expectedResult, actualResult.leftOrNull())
    }
}