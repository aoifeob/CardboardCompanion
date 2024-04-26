package com.example.cardboardcompanion.model.card

import com.example.cardboardcompanion.R

data class Card(
    var id: Long,
    var name: String,
    var set: String,
    var collectorNo: Int,
    var image: Int,
    var price: Double,
    var quantity: Int,
    var colours: List<CardColour>
) {

    fun getDisplayPrice(): String {
        return "€%.${2}f".format(price)
    }

    fun getDisplayName(): String {
        return "($set) $name"
    }

}

data class CardCollection(var collection: List<Card>){
    fun isEmpty() : Boolean {
        return collection.isEmpty()
    }
}

enum class CardColour(var display: String, var image: Int) {
    WHITE("White", R.drawable.mtg_colour_white),
    BLUE("Blue", R.drawable.mtg_colour_blue),
    BLACK("Black", R.drawable.mtg_colour_black),
    RED("Red", R.drawable.mtg_colour_red),
    GREEN("Green", R.drawable.mtg_colour_green),
    COLOURLESS("Colourless", R.drawable.mtg_colour_colourless)
}

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
        return "€$price"
    }
}