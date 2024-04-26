package com.example.cardboardcompanion.model.card

data class DetectedCard(
    var name: String,
    var set: String,
    var collectorNo: String,
    var price: Double
) {

    fun getDisplayDetails(): String {
        return "$name ($set $collectorNo)"
    }

    fun getDisplayPrice(): String {
        return String.format("€%.2f", price)
    }

}
