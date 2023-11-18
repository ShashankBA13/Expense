package com.expensey.ui.screens.accounts.config.screens

import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController

@Composable
fun CreditCardScreen(navController : NavHostController, creditCardId : Int?) {

	Surface {
		Text(
			text = "Welcome to credit card screen"
		)
	}
}