package com.expensey.ui.screens.accounts.config.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBackIos
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.expensey.data.models.BankAccount
import com.expensey.ui.screens.accounts.AccountsViewModel
import com.expensey.ui.theme.ExpenseyTheme
import com.expensey.ui.theme.Typography

@Composable
fun BankAccountsScreen(navHostController : NavHostController, accountId : Int?) {
	val viewModel: AccountsViewModel = viewModel()

	val bankAccount = accountId?.let { viewModel.getBankAccountById(it) }

	var accountName by remember { mutableStateOf("") }
	var currentBalance by remember { mutableStateOf("") }
	var accountNumber by remember { mutableStateOf("") }
	var accountHolderName by remember { mutableStateOf("") }

	Surface(
		modifier = Modifier.fillMaxSize()
	) {
		Column {
			Row(
				modifier = Modifier.fillMaxWidth()
			) {
				Icon(
					imageVector = Icons.Outlined.ArrowBackIos,
					contentDescription = "Back",
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

			Column(
				modifier = Modifier.fillMaxWidth()
			) {
				TextField(
					value = accountName,
					onValueChange = { accountName = it },
					label = { Text("Account Name") }
				)

				TextField(
					value = currentBalance,
					onValueChange = { currentBalance = it },
					label = { Text("Current Balance") }
				)

				TextField(
					value = accountNumber,
					onValueChange = { accountNumber = it },
					label = { Text("Account Number") }
				)

				TextField(
					value = accountHolderName,
					onValueChange = { accountHolderName = it },
					label = { Text("Account Holder Name") }
				)
			}

			Row(
				modifier = Modifier
					.fillMaxWidth()
					.padding(20.dp),
				horizontalArrangement = Arrangement.SpaceEvenly
			) {
				TextButton(
					onClick = {
						navHostController.popBackStack()
					},
					modifier = Modifier.weight(1f)
				) {
					Text("Cancel")
				}

				OutlinedButton(
					onClick = {
						val newBankAccount = BankAccount(
							accountName = accountName,
							currentBalance = currentBalance.toDouble(),
							accountNumber = accountNumber,
							accountHolderName = accountHolderName
						)

						if (newBankAccount.accountId != null) {
							viewModel.updateBankAccount(newBankAccount)
						} else {
							viewModel.insertBankAccount(newBankAccount)
						}
						navHostController.popBackStack()
					},
					modifier = Modifier.weight(1f)
				) {
					Text("Save")
				}
			}
		}
	}
}

@Preview
@Composable
fun PreviewBankAccount() {
	val navHostController: NavHostController = rememberNavController()
	ExpenseyTheme {
		BankAccountsScreen(navHostController = navHostController, 1)
	}
}