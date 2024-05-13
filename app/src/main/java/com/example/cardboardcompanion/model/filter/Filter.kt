package com.example.cardboardcompanion.model.filter

import com.example.cardboardcompanion.model.card.CardColour

data class Filter (
    val minPrice: Double?,
    val maxPrice: Double?,
    val sets: List<String>?,
    val colours: List<CardColour>?
)
