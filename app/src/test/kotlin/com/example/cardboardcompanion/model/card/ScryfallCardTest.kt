package com.example.cardboardcompanion.model.card

import com.example.cardboardcompanion.fixture.getTestCard
import com.example.cardboardcompanion.fixture.getTestScryfallCard
import com.example.cardboardcompanion.fixture.testScryfallCard
import org.junit.Assert.assertEquals
import org.junit.Test

class ScryfallCardTest {

    @Test
    fun scryfallCard_mapToCard_shouldMapScryfallCardToCard() {
        val expected = getTestCard()

        val actual = testScryfallCard.mapToCard()

        assertEquals(expected, actual)
    }

    @Test
    fun scryfallCard_getDisplayPrice_shouldFormatWithEuroSymbolAnd2DecimalPlaces() {
        val expected = "€5.00"
        val scryfallCard = getTestScryfallCard(price = 5.0)

        val actual = scryfallCard.getDisplayPrice()

        assertEquals(expected, actual)
    }

    @Test
    fun scryfallCard_getDisplayDetails_shouldFormatNameWithSetAndCollectorNo() {
        val expected = "Lightning Bolt (CLB 187)"
        val scryfallCard = getTestScryfallCard(name = "Lightning Bolt", set = "clb", collectorNo ="187")

        val actual = scryfallCard.getDisplayDetails()

        assertEquals(expected, actual)
    }
}