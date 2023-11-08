package com.expensey.ui.screens.expense

import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController

@Composable
fun ExpenseScreen(navHostController : NavHostController) {



	Surface {
		Text(text = "Welcome to expense screen")
	}

}