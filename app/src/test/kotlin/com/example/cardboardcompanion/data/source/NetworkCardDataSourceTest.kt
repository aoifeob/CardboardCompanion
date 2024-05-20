package com.example.cardboardcompanion.data.source

import com.example.cardboardcompanion.data.sevice.ScryfallApiService
import com.example.cardboardcompanion.fixture.testCollectorNo
import com.example.cardboardcompanion.fixture.testScryfallCard
import com.example.cardboardcompanion.fixture.testSet
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

class NetworkCardDataSourceTest {

    private val mockApi = mockk<ScryfallApiService>()
    private val dataSource = NetworkCardDataSource(mockApi)

    @Test
    fun networkCardDataSource_validateCard_shouldValidateCard_whenSetAndCollectorNoAreValid() =
        runTest {
            val expectedResult = testScryfallCard
            val set = testSet
            val collectorNo = testCollectorNo
            coEvery { mockApi.getCard(set, collectorNo) } returns Response.success(expectedResult)

            val actualResult = dataSource.validateCard(set, collectorNo)

            assertNotNull(actualResult.getOrNull())
            assertEquals(expectedResult, actualResult.getOrNull()!!)
        }

    @Test
    fun networkCardDataSource_validateCard_shouldReturnCardNotFoundError_whenApiRequestNotSuccessful() =
        runTest {
            val expectedResult = CardValidationError.CardNotFoundError
            val set = testSet
            val collectorNo = testCollectorNo
            coEvery { mockApi.getCard(set, collectorNo) } returns Response.error(
                404, "".toResponseBody("application/json".toMediaTypeOrNull())
            )

            val actualResult = dataSource.validateCard(set, collectorNo)

            assertNotNull(actualResult.leftOrNull())
            assertEquals(expectedResult, actualResult.leftOrNull()!!)
        }

    @Test
    fun networkCardDataSource_validateCard_shouldReturnNetworkError_whenIOExceptionThrown() =
        runTest {
            val expectedResult = CardValidationError.NetworkError
            val set = testSet
            val collectorNo = testCollectorNo
            coEvery { mockApi.getCard(set, collectorNo) } throws IOException()

            val actualResult = dataSource.validateCard(set, collectorNo)

            assertNotNull(actualResult.leftOrNull())
            assertEquals(expectedResult, actualResult.leftOrNull()!!)
        }

    @Test
    fun networkCardDataSource_validateCard_shouldReturnNetworkError_whenHttpExceptionThrown() =
        runTest {
            val expectedResult = CardValidationError.NetworkError
            val set = testSet
            val collectorNo = testCollectorNo
            coEvery { mockApi.getCard(set, collectorNo) } throws HttpException(Response.success(""))

            val actualResult = dataSource.validateCard(set, collectorNo)

            assertNotNull(actualResult.leftOrNull())
            assertEquals(expectedResult, actualResult.leftOrNull()!!)
        }

}