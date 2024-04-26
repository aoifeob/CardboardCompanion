package com.example.cardboardcompanion.model.filter

import com.example.cardboardcompanion.model.card.CardColour

interface Filter {
    var type: FilterType
}

data class PriceFilter(
    override var type: FilterType = FilterType.PRICE,
    val minPrice: Double,
    val maxPrice: Double
) : Filter

data class SetFilter(
    override var type: FilterType = FilterType.SET,
    val set: String
) : Filter

data class ColourFilter(
    override var type: FilterType = FilterType.COLOUR,
    val colours: List<CardColour>
) : Filter

enum class FilterType(var display: String) {
    PRICE ("Price"),
    SET ("Set"),
    COLOUR("Colour");
}