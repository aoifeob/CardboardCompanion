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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.cardboardcompanion.R
import com.example.cardboardcompanion.model.card.Card
import com.example.cardboardcompanion.ui.component.CameraContent
import com.example.cardboardcompanion.ui.theme.CardboardCompanionTheme

@Composable
fun ScannerScreen() {
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
fun ConfirmCard(card: Card) {
    Dialog(onDismissRequest = { /*TODO*/ }) {
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
                        onClick = { /*TODO*/ },
                        modifier = Modifier.padding(8.dp),

                        ) {
                        Text(
                            "Cancel", style = MaterialTheme.typography.bodyLarge
                        )
                    }
                    TextButton(
                        onClick = { /*TODO*/ },
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
    ConfirmCard(
        Card(
            1,
            "Lightning Bolt",
            "2X2",
            117,
            R.drawable.card_lightning_bolt_2x2_117,
            2.30,
            1
        )
    )
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

