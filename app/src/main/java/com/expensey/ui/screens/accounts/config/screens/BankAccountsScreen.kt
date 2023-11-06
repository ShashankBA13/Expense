package com.expensey.ui.screens.accounts.config.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBackIos
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.expensey.ui.screens.accounts.AccountsViewModel
import com.expensey.ui.theme.ExpenseyTheme
import com.expensey.ui.theme.Typography

@Composable
fun BankAccountsScreen(navHostController : NavHostController) {

	val viewModel: AccountsViewModel = viewModel()

	Surface(
		modifier = Modifier.fillMaxSize()
	) {
		Row {
			Icon(
				imageVector = Icons.Outlined.ArrowBackIos,
				contentDescription = "Back", // Provide a content description as needed
				modifier = Modifier.clickable {
					navHostController.popBackStack()
				} then Modifier.padding(start = 20.dp, top = 20.dp, end = 10.dp)
			)

			Text(
				text = "Bank Account",
				modifier = Modifier
					.padding(20.dp)
					.weight(1f),
				style = Typography.headlineLarge
			)
		}
	}

}

@Preview
@Composable
fun PreviewBankAccount() {
	val navHostController: NavHostController = rememberNavController()
	ExpenseyTheme {
		BankAccountsScreen(navHostController = navHostController)
	}
}