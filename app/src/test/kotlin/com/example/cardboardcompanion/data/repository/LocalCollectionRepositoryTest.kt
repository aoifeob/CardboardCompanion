package com.example.cardboardcompanion.data.repository

import com.example.cardboardcompanion.fixture.testCollection
import com.example.cardboardcompanion.fixture.testTop5Collection
import com.example.cardboardcompanion.model.SortParam
import com.example.cardboardcompanion.model.card.CardDao
import com.example.cardboardcompanion.model.filter.Filter
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class LocalCollectionRepositoryTest {

    private val mockDao = mockk<CardDao>()
    private val repository = LocalCollectionRepository(mockDao)

    @Test
    fun localCollectionRepository_getOwnedCards_shouldReturnOwnedCards_whenCardsInCollection() = runTest {
        val expectedResult = testCollection
        coEvery { mockDao.getOwnedCards() } returns flow {emit(expectedResult)}

        val actualResult  = repository.getOwnedCards().first()

        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun localCollectionRepository_getOwnedCards_shouldReturnEmptyList_whenCollectionIsEmpty() = runTest {
        coEvery { mockDao.getOwnedCards() } returns flow {emit(emptyList())}

        val actualResult  = repository.getOwnedCards().first()

        assertTrue(actualResult.isEmpty())
    }

    @Test
    fun localCollectionRepository_getTopOwnedCards_shouldReturnTop5OwnedCards_whenCardsInCollection() = runTest {
        val expectedResult = testTop5Collection
        coEvery { mockDao.getTopOwnedCardsByPrice() } returns flow {emit(expectedResult)}

        val actualResult  = repository.getTopOwnedCardsByPrice().first()

        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun localCollectionRepository_getTopOwnedCards_shouldReturnTop5OwnedCards_whenCollectionIsEmpty() = runTest {
        coEvery { mockDao.getTopOwnedCardsByPrice() } returns flow {emit(emptyList())}

        val actualResult  = repository.getTopOwnedCardsByPrice().first()

        assertTrue(actualResult.isEmpty())
    }

    @Test
    fun localCollectionRepository_addCard_shouldAddNewCardToDatabase_whenCardDoesNotExistInDatabase() = runTest {
        val spyDao = spyk<CardDao>()
        val repositoryWithSpy = LocalCollectionRepository(spyDao)
        val newCard = testCollection[0]

        repositoryWithSpy.addCard(newCard)

        verify { spyDao.addCard(any()) }
    }

    @Test
    fun localCollectionRepository_updateCard_shouldUpdateCardInDatabase_whenCardExistsInDatabase() = runTest {
        val spyDao = spyk<CardDao>()
        val repositoryWithSpy = LocalCollectionRepository(spyDao)
        val newCard = testCollection[0]

        repositoryWithSpy.updateCardQuantity(newCard)

        verify { spyDao.updateCard(any()) }
    }

    @Test
    fun localCollectionRepository_updateCardQuantity_shouldUpdateCardQuantityInDatabase_whenCardExistsInDatabase() = runTest {
        val spyDao = spyk<CardDao>()
        val repositoryWithSpy = LocalCollectionRepository(spyDao)
        val newCard = testCollection[0]

        repositoryWithSpy.updateCardQuantity(newCard)

        verify { spyDao.updateCard(any()) }
    }

    @Test
    fun localCollectionRepository_getOwnedCards_shouldReturnOwnedCardsContainingSearchString_whenSearchStringNotEmpty() = runTest {
        val searchParam = "Medic"
        val filter = null
        val sortParam = SortParam.NAME_ASC
        val expectedResult = testCollection.filter { it.name.contains(searchParam, ignoreCase = true) }
        coEvery { mockDao.getOwnedCards() } returns flow {emit(testCollection)}

        val actualResult  = repository.getOwnedCards(searchParam, filter, sortParam).first()

        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun localCollectionRepository_getOwnedCards_shouldReturnOwnedCardsContainingSearchString_whenSearchStringNotEmptyAndCaseDoesNotMatch() = runTest {
        val searchParam = "medIC"
        val filter = null
        val sortParam = SortParam.NAME_ASC
        val expectedResult = testCollection.filter { it.name.contains(searchParam, ignoreCase = true) }
        coEvery { mockDao.getOwnedCards() } returns flow {emit(testCollection)}

        val actualResult  = repository.getOwnedCards(searchParam, filter, sortParam).first()

        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun localCollectionRepository_getOwnedCards_shouldReturnOwnedCardsMatchingMinPriceOrHigher_whenMinPriceFilterSupplied() = runTest {
        val searchParam = ""
        val filter = Filter(5.00, null)
        val sortParam = SortParam.NAME_ASC
        val expectedResult = testCollection.filter { it.price >= filter.minPrice!! }.sortedBy { it.name }
        coEvery { mockDao.getOwnedCards() } returns flow {emit(testCollection)}

        val actualResult  = repository.getOwnedCards(searchParam, filter, sortParam).first()

        assertEquals(expectedResult.size, actualResult.size)
    }

    @Test
    fun localCollectionRepository_getOwnedCards_shouldReturnOwnedCardsMatchingMaxPriceOrLower_whenMaxPriceFilterSupplied() = runTest {
        val searchParam = ""
        val filter = Filter(null, 5.00)
        val sortParam = SortParam.NAME_ASC
        val expectedResult = testCollection.filter { it.price <= filter.maxPrice!! }.sortedBy { it.name }
        coEvery { mockDao.getOwnedCards() } returns flow {emit(testCollection)}

        val actualResult  = repository.getOwnedCards(searchParam, filter, sortParam).first()

        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun localCollectionRepository_getOwnedCards_shouldReturnOwnedCardsWithPriceRange_whenMinPriceAndMaxPriceFilterSupplied() = runTest {
        val searchParam = ""
        val filter = Filter(5.00, 25.00)
        val sortParam = SortParam.NAME_ASC
        val expectedResult = testCollection.filter { it.price <= filter.maxPrice!! && it.price >= filter.minPrice!! }.sortedBy { it.name }
        coEvery { mockDao.getOwnedCards() } returns flow {emit(testCollection)}

        val actualResult  = repository.getOwnedCards(searchParam, filter, sortParam).first()

        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun localCollectionRepository_getOwnedCards_shouldReturnOwnedCardsByNameAsc_whenSortParamNameAToZ() = runTest {
        val expectedResult = testCollection.sortedBy { it.name }
        val searchParam = ""
        val filter = null
        val sortParam = SortParam.NAME_ASC
        coEvery { mockDao.getOwnedCards() } returns flow {emit(testCollection)}

        val actualResult  = repository.getOwnedCards(searchParam, filter, sortParam).first()

        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun localCollectionRepository_getOwnedCards_shouldReturnOwnedCardsByNameAsc_whenSortParamNameZToA() = runTest {
        val expectedResult = testCollection.sortedByDescending { it.name }
        val searchParam = ""
        val filter = null
        val sortParam = SortParam.NAME_DESC
        coEvery { mockDao.getOwnedCards() } returns flow {emit(testCollection)}

        val actualResult  = repository.getOwnedCards(searchParam, filter, sortParam).first()

        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun localCollectionRepository_getOwnedCards_shouldReturnOwnedCardsByNameAsc_whenSortParamSetAToZ() = runTest {
        val expectedResult = testCollection.sortedBy { it.set }
        val searchParam = ""
        val filter = null
        val sortParam = SortParam.SET_ASC
        coEvery { mockDao.getOwnedCards() } returns flow {emit(testCollection)}

        val actualResult  = repository.getOwnedCards(searchParam, filter, sortParam).first()

        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun localCollectionRepository_getOwnedCards_shouldReturnOwnedCardsByNameAsc_whenSortParamSetZToA() = runTest {
        val expectedResult = testCollection.sortedByDescending { it.set }
        val searchParam = ""
        val filter = null
        val sortParam = SortParam.SET_DESC
        coEvery { mockDao.getOwnedCards() } returns flow {emit(testCollection)}

        val actualResult  = repository.getOwnedCards(searchParam, filter, sortParam).first()

        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun localCollectionRepository_getOwnedCards_shouldReturnOwnedCardsByNameAsc_whenSortParamPriceHighToLow() = runTest {
        val expectedResult = testCollection.sortedByDescending { it.price }
        val searchParam = ""
        val filter = null
        val sortParam = SortParam.PRICE_DESC
        coEvery { mockDao.getOwnedCards() } returns flow {emit(testCollection)}

        val actualResult  = repository.getOwnedCards(searchParam, filter, sortParam).first()

        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun localCollectionRepository_getOwnedCards_shouldReturnOwnedCardsByNameAsc_whenSortParamLowToHigh() = runTest {
        val expectedResult = testCollection.sortedBy { it.price }
        val searchParam = ""
        val filter = null
        val sortParam = SortParam.PRICE_ASC
        coEvery { mockDao.getOwnedCards() } returns flow {emit(testCollection)}

        val actualResult  = repository.getOwnedCards(searchParam, filter, sortParam).first()

        assertEquals(expectedResult, actualResult)
    }

}