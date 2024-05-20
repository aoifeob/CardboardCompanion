package com.example.cardboardcompanion.model.card

import com.example.cardboardcompanion.fixture.getTestCard
import org.junit.Assert.assertEquals
import org.junit.Test

class CardTest {

    @Test
    fun card_getDisplayPrice_shouldFormatWithEuroSymbolAnd2DecimalPlaces() {
        val expected = "€5.00"
        val card = getTestCard(price = 5.0)

        val actual = card.getDisplayPrice()

        assertEquals(expected, actual)
    }

    @Test
    fun card_getDisplayName_shouldFormatSetWithName() {
        val expected = "(CLB) Lightning Bolt"
        val card = getTestCard(name = "Lightning Bolt", set="clb")

        val actual = card.getDisplayName()

        assertEquals(expected, actual)
    }
}