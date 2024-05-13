package com.example.cardboardcompanion.ui.screen

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cardboardcompanion.model.card.Card
import com.example.cardboardcompanion.ui.component.OnboardingScreen
import com.example.cardboardcompanion.ui.theme.CardboardCompanionTheme
import com.example.cardboardcompanion.viewmodel.InsightsViewModel

@Composable
fun InsightsLayout() {
    InsightsLayout(
        modifier = Modifier,
        insightsViewModel = viewModel()
    )
}

@Composable
private fun InsightsLayout(
    insightsViewModel: InsightsViewModel = viewModel(),
    modifier: Modifier
) {
    val insightsUiState by insightsViewModel.uiState.collectAsState()
    val collectionValue by rememberSaveable { mutableStateOf(insightsUiState.collectionValue) }
    val topCards by rememberSaveable { mutableStateOf(insightsUiState.topCards) }
    val isLibraryEmpty by rememberSaveable { mutableStateOf(insightsUiState.topCards.isEmpty()) }

    Column(modifier = modifier) {
        Surface(modifier = modifier.weight(1f), color = MaterialTheme.colorScheme.background) {
            if (isLibraryEmpty) {
                OnboardingScreen()
            } else {
                InsightsScreen(collectionValue, topCards)
            }
        }
    }
}

@Composable
private fun InsightsScreen(
    collectionValue: String,
    topCards: List<Card>,
) {
    Column(
        modifier = Modifier
            .padding(all = 10.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CollectionValueDisplay(collectionValue)
        if (topCards.size < 5) {
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "Add more cards to your collection to see more insights.",
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(all = 4.dp),
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.weight(1f))
        } else {
            Spacer(modifier = Modifier.padding(15.dp))
            Text(
                text = "Top 5 Cards",
                modifier = Modifier.padding(all = 4.dp),
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.titleLarge
            )
            TopCardsCollection(topCards)
        }
    }
}

@Composable
private fun CollectionValueDisplay(collectionValue: String) {
    Text(
        text = "Total collection value: $collectionValue",
        modifier = Modifier.padding(all = 4.dp),
        color = MaterialTheme.colorScheme.primary,
        style = MaterialTheme.typography.titleLarge
    )
}

@Composable
private fun TopCardsCollection(cards: List<Card>) {
    Column(
        modifier = Modifier
            .padding(all = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row {
            Card(cards[0])
            Card(cards[1])
        }
        Row {
            Card(cards[2])
            Card(cards[3])
        }
        Row {
            Card(cards[4])
        }
    }
}

@Composable
private fun Card(card: Card) {
    Column(
        modifier = Modifier.padding(all = 10.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row {
            CardDisplay(card)
        }
        Spacer(modifier = Modifier.height(4.dp))
    }
}

@Composable
private fun CardDisplay(card: Card) {
    Column(
        modifier = Modifier.padding(all = 3.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = card.image),
            contentDescription = null,
            modifier = Modifier
                .size(150.dp)
        )
        Spacer(modifier = Modifier.padding(all = 5.dp))
        Surface(
            shape = MaterialTheme.shapes.medium,
            shadowElevation = 1.dp,
            color = MaterialTheme.colorScheme.background
        ) {
            Text(
                text = card.getDisplayPrice(),
                modifier = Modifier.padding(all = 4.dp),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )
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
private fun InsightsViewPreview() {
    CardboardCompanionTheme {
        InsightsLayout()
    }
}