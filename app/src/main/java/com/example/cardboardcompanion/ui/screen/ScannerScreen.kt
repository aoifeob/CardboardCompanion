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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.cardboardcompanion.model.card.DetectedCard
import com.example.cardboardcompanion.ui.component.CameraContent
import com.example.cardboardcompanion.ui.theme.CardboardCompanionTheme

@Composable
fun ScannerScreen() {
    var isConfirmDialogVisible by remember { mutableStateOf(false) }

    CardboardCompanionTheme {
        IdentifyScreen()
    }
}

@Composable
fun IdentifyScreen() {
    CameraContent()
    Column {
        Spacer(modifier = Modifier.weight(1f))
        Button(
            onClick = {
                /* TODO */
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
fun ConfirmCard(card: DetectedCard, onDismiss: () -> Unit, onConfirm: () -> Unit) {
    Dialog(onDismissRequest = { onDismiss() }) {
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
                    text = card.getDisplayDetails() + "\n" +
                            "Price: " + card.getDisplayPrice() + "\n" +
                            "Add card to your collection?",
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    TextButton(
                        onClick = { onDismiss() },
                        modifier = Modifier.padding(8.dp),

                        ) {
                        Text(
                            "Cancel", style = MaterialTheme.typography.bodyLarge
                        )
                    }
                    TextButton(
                        onClick = {
                            onConfirm()
                        },
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

@Preview(name = "Light Mode")
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
    name = "Dark Mode"
)
@Composable
fun ConfirmPreview() {
    var isConfirmDialogVisible by remember { mutableStateOf(true) }

    ConfirmCard(
        DetectedCard("Argivian Phalanx", "DMU", "5", 0.05),
        { isConfirmDialogVisible = false },
        {
            isConfirmDialogVisible =
                false /*TODO: add card to library and indicate if failure occurs with toast*/
        })
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

