package com.expensey.ui.screens.insights

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavHostController
import com.expensey.ui.theme.Typography

@Composable
fun InsightsScreen(navController: NavHostController) {
	Surface {
		Column(
			modifier = Modifier.fillMaxSize(),
			verticalArrangement = Arrangement.Center,
			horizontalAlignment = Alignment.CenterHorizontally
		) {
			Text(
				text = "Screen is under development. Get insights on your expenses",
				modifier = Modifier.fillMaxSize(),
				textAlign = TextAlign.Center,
				style = Typography.titleLarge,
				color = MaterialTheme.colorScheme.secondary
			)
		}
	}
}
