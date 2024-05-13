package com.example.cardboardcompanion.ui.screen

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cardboardcompanion.R
import com.example.cardboardcompanion.model.SortParam
import com.example.cardboardcompanion.model.card.Card
import com.example.cardboardcompanion.ui.component.OnboardingScreen
import com.example.cardboardcompanion.ui.theme.CardboardCompanionTheme
import com.example.cardboardcompanion.viewmodel.CollectionViewModel

@Composable
fun CollectionLayout() {
    CollectionLayout(
        modifier = Modifier,
        collectionViewModel = viewModel()
    )
}

@Composable
private fun CollectionLayout(
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
                    cards, collectionViewModel.searchParam,
                    collectionViewModel.isActiveSearch,
                    { collectionViewModel.onSearchParamUpdated(it) },
                    { collectionViewModel.onSearchExecuted(it) },
                    collectionViewModel.sortParam,
                    { collectionViewModel.onSortExecuted(it) }
                )
            }
        }
    }
}

@Composable
private fun CollectionScreen(
    cards: List<Card>,
    searchParam: String,
    isActiveSearch: Boolean,
    onSearchParamUpdated: (String) -> Unit,
    onSearchExecuted: (String) -> Unit,
    sortParam: SortParam,
    onSortParamUpdated: (SortParam) -> Unit
) {

    Column {

        CustomiseResultsMenu(
            searchParam,
            onSearchParamUpdated,
            onSearchExecuted,
            sortParam,
            onSortParamUpdated
        )

        if (isActiveSearch && searchParam.isNotBlank()) {
            if (cards.isEmpty()) {
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    "No results found for: $searchParam",
                    fontStyle = FontStyle.Italic,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .padding(15.dp)
                        .align(Alignment.CenterHorizontally)
                )
                Spacer(modifier = Modifier.weight(1f))
            } else {
                Text(
                    "Displaying results for: $searchParam",
                    fontStyle = FontStyle.Italic,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .padding(top = 25.dp, bottom = 5.dp)
                        .align(Alignment.CenterHorizontally)
                )
            }
        }

        CardCollection(cards)

    }
}

@Composable
private fun CustomiseResultsMenu(
    searchParam: String,
    onSearchParamUpdated: (String) -> Unit,
    onSearchExecuted: (String) -> Unit,
    sortParam: SortParam,
    onSortParamUpdated: (SortParam) -> Unit
) {
    Surface(color = MaterialTheme.colorScheme.secondary, modifier = Modifier.fillMaxWidth()) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            SearchMenu(searchParam, onSearchParamUpdated, onSearchExecuted)
            Spacer(modifier = Modifier.weight(1f))
            FilterMenu()
            Spacer(modifier = Modifier.weight(1f))
            SortMenu(sortParam, onSortParamUpdated)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchMenu(
    searchParam: String,
    onSearchParamUpdated: (String) -> Unit,
    onSearchExecuted: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    Button(
        onClick = { expanded = !expanded },
        colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.secondary)
    ) {
        Icon(imageVector = Icons.Default.Search, contentDescription = null)
    }

    if (expanded) {
        Box () {
            SearchBar(
                query = searchParam,
                onQueryChange = onSearchParamUpdated,
                onSearch = onSearchExecuted,
                placeholder = {
                    Text(text = "e.g. Bolt", fontStyle = FontStyle.Italic)
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        tint = MaterialTheme.colorScheme.onSurface,
                        contentDescription = null
                    )
                },
                content = {},
                active = true,
                onActiveChange = {  },
            )
        }
    }

    /*TODO: display search menu when expanded:
        - Search by card name
        - Search by set name
     */
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
private fun SortMenu(sortParam: SortParam, onSortParamUpdated: (SortParam) -> Unit) {
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

    Box {
        DropdownMenu(expanded = expanded,
            onDismissRequest = { expanded = !expanded }) {
            SortParam.entries.forEach {
                DropdownMenuItem(text = {
                    Text(
                        it.display,
                        color = if (sortParam == it) MaterialTheme.colorScheme.primary else Color.Black,
                    )
                },
                    onClick = {
                        expanded = !expanded
                        if (sortParam != it) {
                            onSortParamUpdated(it)
                        }
                    })
            }
        }

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