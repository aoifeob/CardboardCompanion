package com.example.cardboardcompanion.ui.navigation

import androidx.compose.runtime.Composable
import com.example.cardboardcompanion.R
import com.example.cardboardcompanion.ui.screen.TutorialScreen

interface Destination {
    val icon: Int
    val title: String
    val route: String
    val screen: @Composable () -> Unit
}

object Tutorial : Destination {
    override val icon = R.drawable.baseline_question_mark_24
    override val title = "How To Use"
    override val route = "tutorial"
    override val screen: @Composable () -> Unit = { TutorialScreen() }
}

val topBarNavScreens = listOf(Tutorial)

