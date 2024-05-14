package com.example.cardboardcompanion.model.filter

data class Filter (
    val minPrice: Double?,
    val maxPrice: Double?,
    val sets: List<String>?
)