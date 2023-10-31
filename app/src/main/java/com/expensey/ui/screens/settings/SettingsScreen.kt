package com.expensey.ui.screens.settings

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.expensey.ui.theme.ExpenseyTheme

@Composable
fun SettingsScreen() {
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = "Welcome to the Settings Screen!",
            modifier = Modifier.padding(16.dp) // Add padding if needed
        )
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    ExpenseyTheme {
        SettingsScreen()
    }
}