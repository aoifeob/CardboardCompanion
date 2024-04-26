package com.example.cardboardcompanion.ui.navigation

import androidx.compose.runtime.Composable
import com.example.cardboardcompanion.R
import com.example.cardboardcompanion.ui.screen.CollectionLayout
import com.example.cardboardcompanion.ui.screen.ScannerScreen
import com.example.cardboardcompanion.ui.screen.TutorialScreen

interface Destination {
    val icon: Int
    val title: String
    val route: String
    val screen: @Composable () -> Unit
}

object Scanner : Destination {
    override val icon = R.drawable.baseline_photo_camera_24
    override val title = "Scan New Card"
    override val route = "scanner"
    override val screen: @Composable () -> Unit = { ScannerScreen() }
}

object Collection : Destination {
    override val icon = R.drawable.baseline_folder_24
    override val title = "My Collection"
    override val route = "collection"
    override val screen: @Composable () -> Unit = { CollectionLayout() }
}

object Tutorial : Destination {
    override val icon = R.drawable.baseline_question_mark_24
    override val title = "How To Use"
    override val route = "tutorial"
    override val screen: @Composable () -> Unit = { TutorialScreen() }
}

val topBarNavScreens = listOf(Scanner, Collection, Tutorial)

