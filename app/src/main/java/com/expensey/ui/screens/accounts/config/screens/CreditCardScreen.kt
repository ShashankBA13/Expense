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
import com.expensey.data.models.CreditCard
import com.expensey.ui.screens.accounts.AccountsViewModel
import com.expensey.ui.theme.ExpenseyTheme
import com.expensey.ui.theme.Typography
import java.util.Date

@Composable
fun CreditCardScreen(navHostController : NavHostController, creditCardId : Int?) {

	val viewModel : AccountsViewModel = viewModel()

	var creditCard by remember {
		mutableStateOf<CreditCard?>(null)
	}

	val creditCardLiveData = if (creditCardId != null && creditCardId != 0) {
		viewModel.fetchCreditCardById(creditCardId)
	} else {
		null
	}

	creditCardLiveData?.observeAsState()?.value?.let { creditCard = it }

	var creditCardName by remember {
		mutableStateOf(creditCard?.name.orEmpty())
	}

	var totalLimit by remember {
		mutableStateOf(creditCard?.totalLimit?.toString() ?: "")
	}

	var availableLimit by remember {
		mutableStateOf(creditCard?.currentBalance?.toString() ?: "")
	}

	var cardHolder by remember {
		mutableStateOf(creditCard?.cardHolder.orEmpty())
	}

	if (creditCardLiveData != null) {
		creditCardName = creditCard?.name.toString()
		totalLimit = creditCard?.totalLimit.toString()
		availableLimit = creditCard?.currentBalance.toString()
		cardHolder = creditCard?.cardHolder.toString()
	}


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
					text = "Credit Card",
					modifier = Modifier
						.padding(20.dp)
						.weight(1f),
					style = Typography.headlineLarge
				)

				if (creditCardId != null && creditCardId != 0) {
					Icon(
						imageVector = Icons.Outlined.Delete,
						contentDescription = "Delete Credit Card",
						modifier = Modifier.clickable {
							creditCard?.let { viewModel.deleteCreditCard(it) }
							navHostController.popBackStack()
						} then Modifier.padding(start = 20.dp, top = 20.dp, end = 20.dp),
						tint = Color.Red
					)
				}
			}


			Column(
				modifier = Modifier
					.fillMaxWidth()
					.padding(16.dp)
			) {
				OutlinedTextField(
					value = creditCardName,
					onValueChange = { creditCardName = it },
					label = { Text("Card Name") },
					modifier = Modifier
						.fillMaxWidth()
						.padding(bottom = 8.dp)
				)

				OutlinedTextField(
					value = totalLimit,
					onValueChange = { totalLimit = it },
					label = { Text("Total Limit") },
					modifier = Modifier
						.fillMaxWidth()
						.padding(bottom = 8.dp)
				)

				OutlinedTextField(
					value = availableLimit,
					onValueChange = { availableLimit = it },
					label = { Text("Available Limit") },
					modifier = Modifier
						.fillMaxWidth()
						.padding(bottom = 8.dp)
				)

				OutlinedTextField(
					value = cardHolder,
					onValueChange = { cardHolder = it },
					label = { Text("Card Holder") },
					modifier = Modifier
						.fillMaxWidth()
						.padding(bottom = 8.dp)
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

						if (creditCard?.creditCardId != null && creditCardId != null) {
							val updateCreditCard = CreditCard(
								creditCardId = creditCardId,
								name = creditCardName,
								cardHolder = cardHolder,
								currentBalance = availableLimit.toDouble(),
								totalLimit = totalLimit.toDouble(),
								cardNumber = 0,
								billGenerationDate = Date(),
								billPaymentDate = Date(),
								notifyUserOfBillDate = false,
								interestRate = 0.0,
								cvv = 0,
								expirationDate = Date()
							)
							viewModel.updateCreditCard(updateCreditCard)
						} else {
							val newCreditCard = CreditCard(
								name = creditCardName,
								cardHolder = cardHolder,
								currentBalance = availableLimit.toDouble(),
								totalLimit = totalLimit.toDouble(),
								cardNumber = 0,
								billGenerationDate = Date(),
								billPaymentDate = Date(),
								notifyUserOfBillDate = false,
								interestRate = 0.0,
								cvv = 0,
								expirationDate = Date()
							)
							viewModel.insertCreditCard(newCreditCard)

						}
						navHostController.popBackStack()
					},
					modifier = Modifier.weight(1f)
				) {
					Text(if (creditCardId != null && creditCardId != 0) "Update" else "Save")
				}
			}
		}
	}
}

@Preview
@Composable
fun PreviewCreditCard() {
	val navHostController : NavHostController = rememberNavController()
	ExpenseyTheme {
		CreditCardScreen(navHostController = navHostController, 0)
	}
}