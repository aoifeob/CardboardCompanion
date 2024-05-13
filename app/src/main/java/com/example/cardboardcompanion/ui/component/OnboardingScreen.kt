package com.example.cardboardcompanion.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun OnboardingScreen(modifier: Modifier = Modifier) {
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
            onClick = { /* TODO: implement navigation */}
        ) {
            Text("Scan a Card")
        }
    }
}
