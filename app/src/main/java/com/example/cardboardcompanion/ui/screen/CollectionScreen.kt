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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.cardboardcompanion.R
import com.example.cardboardcompanion.model.SortParam
import com.example.cardboardcompanion.model.card.Card
import com.example.cardboardcompanion.model.filter.Filter
import com.example.cardboardcompanion.ui.component.OnboardingScreen
import com.example.cardboardcompanion.ui.theme.CardboardCompanionTheme
import com.example.cardboardcompanion.viewmodel.CollectionViewModel
import kotlin.math.roundToInt

@Composable
fun CollectionLayout() {
    CollectionLayout(
        collectionViewModel = hiltViewModel<CollectionViewModel>(),
        modifier = Modifier
    )
}

@Composable
private fun CollectionLayout(
    collectionViewModel: CollectionViewModel,
    modifier: Modifier
) {
    LaunchedEffect(key1 = true, block = {
        collectionViewModel.getOwnedCards()
    })

    val cards by collectionViewModel.cardCollection.collectAsStateWithLifecycle()
    val isCollectionEmpty by collectionViewModel.isCollectionEmpty.collectAsStateWithLifecycle()

    Column {
        Surface(modifier = modifier.weight(1f), color = MaterialTheme.colorScheme.background) {
            if (isCollectionEmpty) {
                OnboardingScreen()
            } else {
                CollectionScreen(
                    cards, collectionViewModel.searchParam,
                    collectionViewModel.isActiveSearch,
                    { collectionViewModel.onSearchParamUpdated(it) },
                    { collectionViewModel.onSearchExecuted(it) },
                    collectionViewModel.sortParam,
                    { collectionViewModel.onSortExecuted(it) },
                    collectionViewModel.filter,
                    { collectionViewModel.onFilterExecuted(it) }
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
    onSortParamUpdated: (SortParam) -> Unit,
    filter: Filter?,
    onFilterExecuted: (Filter?) -> Unit
) {

    Column {

        CustomiseResultsMenu(
            searchParam,
            onSearchParamUpdated,
            onSearchExecuted,
            sortParam,
            onSortParamUpdated,
            filter,
            onFilterExecuted
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
                    "Found ${cards.size} results for: $searchParam",
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
    onSortParamUpdated: (SortParam) -> Unit,
    filter: Filter?,
    onFilterExecuted: (Filter?) -> Unit
) {
    Surface(color = MaterialTheme.colorScheme.secondary, modifier = Modifier.fillMaxWidth()) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            SearchMenu(searchParam, onSearchParamUpdated, onSearchExecuted)
            Spacer(modifier = Modifier.weight(1f))
            FilterMenu(filter, onFilterExecuted)
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
        Box() {
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
                onActiveChange = { }
            )
        }
    }
}

@Composable
private fun FilterMenu(
    filter: Filter?,
    onFilterExecuted: (Filter?) -> Unit
) {
    var expanded by rememberSaveable { mutableStateOf(false) }

    val currentMinPrice = if (filter?.minPrice == null) 0f else filter.minPrice.toFloat()
    val currentMaxPrice =
        if (filter?.maxPrice == null) 100f else filter.maxPrice.toFloat()
    var sliderPosition by remember { mutableStateOf(currentMinPrice..currentMaxPrice) }

    var minPrice: Double? = filter?.minPrice
    var maxPrice: Double? = filter?.maxPrice

    Button(
        onClick = { expanded = !expanded },
        colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.secondary)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.baseline_filter_alt_24),
            contentDescription = null,
            tint = if (filter != null) MaterialTheme.colorScheme.inversePrimary else MaterialTheme.colorScheme.surface
        )
    }
    if (expanded) {
        Dialog(onDismissRequest = { expanded = !expanded }) {
            androidx.compose.material3.Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(375.dp)
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
            ) {
                Column(
                    modifier = Modifier.padding(all = 10.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "Price Filter",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                    RangeSlider(
                        value = sliderPosition,
                        steps = 20,
                        onValueChange = { range ->
                            sliderPosition =
                                range.start.roundToInt().toFloat()..range.endInclusive.roundToInt()
                                    .toFloat()
                        },
                        valueRange = 0f..100f,
                        onValueChangeFinished = {
                            minPrice =
                                if (sliderPosition.start.toInt() == (0))
                                    null
                                else sliderPosition.start.toDouble()
                            maxPrice =
                                if (sliderPosition.endInclusive.toInt() == (100))
                                    null
                                else sliderPosition.endInclusive.toDouble()
                        },
                    )
                    Text(text = sliderPosition.toString())
                    Spacer(modifier = Modifier.weight(1f))
                    Row(
                        modifier = Modifier.padding(all = 10.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Spacer(modifier = Modifier.weight(1f))
                        Button(
                            onClick = {
                                onFilterExecuted(null)
                                expanded = false
                            },
                            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.secondary)
                        ) {
                            Text("Clear")
                        }
                        Spacer(modifier = Modifier.weight(1f))
                        Button(onClick = {
                            onFilterExecuted(Filter(minPrice, maxPrice, null))
                            expanded = false
                        }) {
                            Text("Apply")
                        }
                        Spacer(modifier = Modifier.weight(1f))
                    }
                    Button(
                        onClick = { expanded = !expanded },
                        colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.error)
                    ) {
                        Text("Cancel")
                    }
                }
            }
        }
    }
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