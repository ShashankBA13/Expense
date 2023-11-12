package com.expensey.ui.screens.accounts.config.screens

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBackIos
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
	val TAG = "BankAccountsScreen"
	val viewModel: AccountsViewModel = viewModel()
	val bankAccountLiveData = if (accountId != null && accountId != 0) {
		viewModel.getBankAccountById(accountId)
	} else {
		null
	}

	var bankAccount by remember { mutableStateOf<BankAccount?>(null) }

	bankAccountLiveData?.observeAsState()?.value?.let { bankAccount = it }

	Log.d(TAG, bankAccount.toString())

	var accountIdd = bankAccount?.accountId
	var accountName = bankAccount?.accountName.orEmpty()
	var currentBalance by remember { mutableStateOf(bankAccount?.currentBalance.toString()) }
	var accountNumber by remember { mutableStateOf(bankAccount?.accountNumber.orEmpty()) }
	var accountHolderName by remember { mutableStateOf(bankAccount?.accountHolderName.orEmpty()) }

	Log.d(TAG, "AccountName: $accountName")
	Log.d(TAG, "CurrentBalance: $currentBalance")
	Log.d(TAG, "accountNumber: $accountNumber")
	Log.d(TAG, "accountHolderName: $accountHolderName")
	Log.d(TAG, "accountId: $accountIdd")

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

				if (accountId != null) {
					Icon(
						imageVector = Icons.Outlined.Delete,
						contentDescription = "Back",
						modifier = Modifier.clickable {
							bankAccount?.let { viewModel.deleteBankAccount(it) }
							navHostController.popBackStack()
						} then Modifier.padding(start = 20.dp, top = 20.dp, end = 20.dp),
						tint = Color.Red
					)
				}
			}

			Column(
				modifier = Modifier.fillMaxWidth()
			) {
				OutlinedTextField(
					value = accountName,
					onValueChange = { accountName = it
						Log.d("TextField", "Value changed: $it")
					},
					label = { Text("Account Name") },
					modifier = Modifier
						.fillMaxWidth()
						.padding(start = 20.dp, end = 20.dp, bottom = 10.dp)
				)

				OutlinedTextField(
					value = currentBalance,
					onValueChange = { currentBalance = it },
					label = { Text("Current Balance") },
					modifier = Modifier
						.fillMaxWidth()
						.padding(start = 20.dp, end = 20.dp, bottom = 10.dp)
				)

				OutlinedTextField(
					value = accountNumber,
					onValueChange = { accountNumber = it },
					label = { Text("Account Number") },
					modifier = Modifier
						.fillMaxWidth()
						.padding(start = 20.dp, end = 20.dp, bottom = 10.dp)
				)

				OutlinedTextField(
					value = accountHolderName,
					onValueChange = { accountHolderName = it },
					label = { Text("Account Holder Name") },
					modifier = Modifier
						.fillMaxWidth()
						.padding(start = 20.dp, end = 20.dp, bottom = 10.dp)
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

						if (bankAccount?.accountId != null && accountId != null) {

							Log.d(TAG, "Inside the if block")
							val oldBankAccount = BankAccount(
								accountId = accountId,
								accountName = accountName,
								currentBalance = currentBalance.toDouble(),
								accountNumber = accountNumber,
								accountHolderName = accountHolderName
							)
							viewModel.updateBankAccount(oldBankAccount)

						} else {

							Log.d(TAG, "Inside the else block")
							val newBankAccount = BankAccount(
								accountName = accountName,
								currentBalance = currentBalance.toDouble(),
								accountNumber = accountNumber,
								accountHolderName = accountHolderName
							)
							viewModel.insertBankAccount(newBankAccount)

						}
						navHostController.popBackStack()
					},
					modifier = Modifier.weight(1f)
				) {
					Text(if (accountId != null) "Update" else "Save")
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