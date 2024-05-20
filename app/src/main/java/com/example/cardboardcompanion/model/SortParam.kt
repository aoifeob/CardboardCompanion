package com.example.cardboardcompanion.model

enum class SortParam(var display: String) {
    NAME_ASC("Name (A-Z)"),
    NAME_DESC("Name (Z-A)"),
    PRICE_DESC ("Price (High-Low)"),
    PRICE_ASC ("Price (Low-High)"),
    SET_ASC ("Set (A-Z)"),
    SET_DESC ("Set (Z-A)")
}