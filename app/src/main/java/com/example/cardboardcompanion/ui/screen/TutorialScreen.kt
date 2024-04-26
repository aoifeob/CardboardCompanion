package com.example.cardboardcompanion.ui.screen

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cardboardcompanion.ui.theme.CardboardCompanionTheme

@Composable
fun TutorialScreen() {
    CardboardCompanionTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Welcome to Cardboard Companion!", style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.padding(15.dp))

                Text(
                    "Cardboard Companion keeps track of the Magic: The Gathering cards in your collection. \n\n" +
                            "Cardboard Companion uses OCR to automatically recognise the card name, set, and collector number of cards scanned using your device's camera.\n\n" +
                            "To add cards to your collection:\n" +
                            "1. Select the \"Scan New Card\" menu option.\n" +
                            "2. Point your device's camera at the card you wish to scan.\n" +
                            "3. Once the card is clearly in focus, click the \"Identify\" button.\n\n" +
                            "You will be given the opportunity to confirm the card details before it is added to your collection.\n\n" +
                            "If you experience any problems with card scanning, please ensure the full card is visible with the camera, and that the image is focused."
                    ,
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}

@Preview(name = "Light Mode")
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
    name = "Dark Mode"
)
@Composable
fun TutorialViewPreview() {
    TutorialScreen()
}