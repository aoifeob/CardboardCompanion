package com.example.cardboardcompanion.ui.state

import com.example.cardboardcompanion.model.card.Card

data class InsightsUiState(
    val collectionValue: String,
    val topCards: List<Card>
)