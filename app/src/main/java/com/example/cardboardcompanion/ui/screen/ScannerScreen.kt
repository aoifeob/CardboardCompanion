package com.example.cardboardcompanion.ui.screen

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.cardboardcompanion.data.source.CardValidationError
import com.example.cardboardcompanion.model.card.ScryfallCard
import com.example.cardboardcompanion.model.card.ScryfallImageSource
import com.example.cardboardcompanion.model.card.ScryfallPrice
import com.example.cardboardcompanion.ui.component.CameraContent
import com.example.cardboardcompanion.ui.theme.CardboardCompanionTheme
import com.example.cardboardcompanion.viewmodel.ScannerViewModel

@Composable
fun ScannerScreen() {
    CardboardCompanionTheme {
        IdentifyScreen(
            viewModel = hiltViewModel<ScannerViewModel>()
        )
    }
}

@Composable
fun IdentifyScreen(
    viewModel: ScannerViewModel
) {
    LaunchedEffect(key1 = true, block = {
        viewModel.getOwnedCards()
    })
    CameraContent(onCardTextDetected = { viewModel.updateDetectedCardText(it) })
    if (viewModel.shouldShowConfirmDialog && viewModel.currentDetectedCard != null) {
        ConfirmCardDialog(
            detectedCard = viewModel.currentDetectedCard!!,
            onDismissRequest = { viewModel.clearDetectedCard() },
            onConfirmAddCard = { viewModel.addCardToCollection() })
    } else if (viewModel.cardValidationError != null) {
        ValidationErrorDialog(
            validationError = viewModel.cardValidationError!!,
            onDismissRequest = { viewModel.clearDetectedCardError() })
    }
    Column {
        Spacer(modifier = Modifier.weight(1f))
        Button(
            onClick = {
                viewModel.validateCard()
            },
            modifier = Modifier
                .alpha(0.75f)
                .padding(10.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(40)
        ) {
            Text("Identify")
        }
    }
}

@Composable
fun ConfirmCardDialog(
    detectedCard: ScryfallCard,
    onDismissRequest: () -> Unit,
    onConfirmAddCard: () -> Unit
) {
    val showDialog = remember {
        mutableStateOf(true)
    }

    if (showDialog.value) {
        Dialog(onDismissRequest = {
            showDialog.value = false
            onDismissRequest()
        }) {
            Card(
                modifier = Modifier
                    .height(200.dp)
                    .padding(8.dp),
                shape = RoundedCornerShape(20),
                border = BorderStroke(1.dp, Color.Black)
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = "Card Detected",
                        modifier = Modifier.padding(16.dp),
                        fontWeight = FontWeight.Black,
                        style = MaterialTheme.typography.titleLarge
                    )
                    Text(
                        text = detectedCard.getDisplayDetails() + "\n" +
                                "Price: " + detectedCard.getDisplayPrice() + "\n" +
                                "Add card to your collection?",
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center
                    )
                    HorizontalDivider()
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        TextButton(
                            onClick = {
                                showDialog.value = false
                                onDismissRequest()
                            },
                            modifier = Modifier.padding(8.dp),

                            ) {
                            Text(
                                "Cancel", style = MaterialTheme.typography.bodyLarge
                            )
                        }
                        VerticalDivider(modifier = Modifier.padding(5.dp))
                        TextButton(
                            onClick = { onConfirmAddCard() },
                            modifier = Modifier.padding(8.dp),
                        ) {
                            Text(
                                "Add Card", style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ValidationErrorDialog(
    validationError: CardValidationError,
    onDismissRequest: () -> Unit
) {
    val showDialog = remember {
        mutableStateOf(true)
    }

    if (showDialog.value) {
        Dialog(onDismissRequest = {
            showDialog.value = false
            onDismissRequest()
        }) {
            Card(
                modifier = Modifier
                    .height(250.dp)
                    .padding(8.dp),
                shape = RoundedCornerShape(20),
                border = BorderStroke(1.dp, Color.Black)
            ) {
                Column(
                    modifier = Modifier.padding(15.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = "Card Validation Error",
                        modifier = Modifier.padding(16.dp),
                        fontWeight = FontWeight.Black,
                        style = MaterialTheme.typography.titleLarge
                    )
                    Text(
                        text = if (validationError == CardValidationError.NetworkError)
                            "Unable to connect to the network.\n" +
                                    "Ensure wifi or data is turned on and try again."
                        else
                            "Unable to identify card.\n" +
                                    "Ensure the whole card is visible on screen and that the image is in focus and try again.",
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center,
                    )
                    HorizontalDivider()
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextButton(
                            onClick = {
                                showDialog.value = false
                                onDismissRequest()
                            },
                            modifier = Modifier.padding(8.dp)
                        ) {
                            Text(
                                "Okay", style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                }
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
fun ConfirmPreview() {
    ConfirmCardDialog(detectedCard = ScryfallCard(
        "Take Up the Shield",
        "otj",
        "34",
        ScryfallPrice(0.10),
        ScryfallImageSource(
            "https://cards.scryfall.io/large/front/7/6/76a31968-ba6d-4c01-838f-4cb8c64e73fb.jpg"
        )
    ),
        onDismissRequest = {},
        onConfirmAddCard = {})
}

@Preview(name = "Light Mode")
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
    name = "Dark Mode"
)
@Composable
fun ScannerViewPreview() {
    ScannerScreen()
}

