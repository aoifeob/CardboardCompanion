package com.example.cardboardcompanion.ui.screen

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cardboardcompanion.R
import com.example.cardboardcompanion.model.card.Card
import com.example.cardboardcompanion.ui.theme.CardboardCompanionTheme
import com.example.cardboardcompanion.viewmodel.CollectionViewModel

@Composable
fun CollectionLayout(
    collectionViewModel: CollectionViewModel = viewModel(),
    modifier: Modifier
) {
    val collectionUiState by collectionViewModel.uiState.collectAsState()
    val cards = collectionViewModel.visibleCards
    val isLibraryEmpty by rememberSaveable { mutableStateOf(collectionUiState.cardCollection.isEmpty()) }

    Column {
        Surface(modifier = modifier.weight(1f), color = MaterialTheme.colorScheme.background) {
            if (isLibraryEmpty) {
                OnboardingScreen()
            } else {
                CollectionScreen(
                    cards
                )
            }
        }
    }
}

@Composable
fun CollectionScreen(
    cards: List<Card>
) {

    Column {

        CustomiseResultsMenu(        )

        CardCollection(cards)

    }
}

@Composable
fun CollectionLayout() {
    CollectionLayout(
        modifier = Modifier,
        collectionViewModel = viewModel()
    )
}

@Composable
private fun CustomiseResultsMenu(
) {
    Surface(color = MaterialTheme.colorScheme.secondary, modifier = Modifier.fillMaxWidth()) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            SearchMenu()
            Spacer(modifier = Modifier.weight(1f))
            FilterMenu()
            Spacer(modifier = Modifier.weight(1f))
            SortMenu()
        }
    }
}

@Composable
private fun SearchMenu(
) {
    var expanded by remember { mutableStateOf(false) }
    Button(
        onClick = { expanded = !expanded },
        colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.secondary)
    ) {
        Icon(imageVector = Icons.Default.Search, contentDescription = null)
    }

    /*TODO: display search menu when expanded:
        - Search by card name
        - Search by set name
     */
}

@Composable
fun SearchBar() {
    var text by remember { mutableStateOf("") }

    TextField(
        value = text,
        onValueChange = { text = it },
        label = { Text("Search") },
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun FilterMenu(
) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    Button(
        onClick = { expanded = !expanded },
        colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.secondary)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.baseline_filter_alt_24),
            contentDescription = null
        )
    }
    /*TODO: display filter menu when expanded:
        - Price
        - Set
        - Colour(s)
    */
}

@Composable
private fun SortMenu() {
    var expanded by rememberSaveable { mutableStateOf(false) }
    Button(
        onClick = { expanded = !expanded },
        colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.secondary)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.baseline_sort_24),
            contentDescription = null
        )
    }

}

@Composable
private fun CardCollection(cards: List<Card>) {
    LazyColumn(
        modifier = Modifier
            .padding(all = 10.dp)
            .fillMaxWidth()
    ) {
        items(cards) { card ->
            Card(card)
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
            CardPreview(card)
        }
        Spacer(modifier = Modifier.height(4.dp))
        CardDetails(card)
    }
}

@Composable
private fun CardPreview(card: Card) {
    Surface {
        Image(
            painter = painterResource(id = card.image),
            contentDescription = null,
            modifier = Modifier
                .size(350.dp)
        )
        if (card.quantity > 1) {
            Surface(
                color = Color.Black,
                modifier = Modifier
                    .padding(horizontal = 40.dp)
                    .size(20.dp)
                    .clip(CircleShape)
                    .border(1.5.dp, Color.White, CircleShape)
            ) {
                Text(
                    text = card.quantity.toString(),
                    style = MaterialTheme.typography.titleSmall,
                    textAlign = TextAlign.Center,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
private fun CardDetails(card: Card) {
    Row(
        modifier = Modifier.padding(all = 8.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = card.getDisplayName(),
            modifier = Modifier.padding(all = 4.dp),
            color = MaterialTheme.colorScheme.secondary,
            style = MaterialTheme.typography.titleMedium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.weight(1f))
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

@Composable
private fun OnboardingScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Welcome to Cardboard Companion!",
            style = MaterialTheme.typography.titleMedium,
        )
        Spacer(modifier = Modifier.padding(15.dp))
        Text(
            "Add some cards to your collection to get started",
            style = MaterialTheme.typography.bodyMedium,
        )
        Spacer(modifier = Modifier.padding(2.dp))
        Button(
            modifier = Modifier.padding(vertical = 24.dp),
            onClick = { /* TODO: redirect to scanner */ }
        ) {
            Text("Scan a Card")
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
private fun CollectionViewPreview() {
    CardboardCompanionTheme {
        CollectionLayout()
    }
}