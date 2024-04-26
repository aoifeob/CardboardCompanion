package com.example.cardboardcompanion.ui.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
fun FolderSettingsMenu() {
    var expanded by remember { mutableStateOf(false) }
    IconButton(onClick = { expanded = !expanded }) {
        Icon(imageVector = Icons.Default.Settings, contentDescription = null)
    }

    /*TODO: display settings menu when expanded:
        - Rename folder
        - Move folder (disabled for parent folder)
        - Delete folder (disabled for parent folder)
     */
}