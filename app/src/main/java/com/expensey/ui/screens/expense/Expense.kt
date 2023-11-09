package com.expensey.ui.screens.expense

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.expensey.data.models.Category
import com.expensey.data.models.Expense
import com.expensey.ui.screens.category.CategoryViewModel
import com.expensey.ui.screens.home.HomeViewModel
import java.util.Date

@Composable
fun ExpenseScreen(navHostController: NavHostController) {

	val categoryViewModel : CategoryViewModel = viewModel()
	val homeViewModel : HomeViewModel = viewModel()

	val categoryLiveDataList : LiveData<List<Category>> = categoryViewModel.categoryLiveDataList

	var descriptionState by remember { mutableStateOf(TextFieldValue("")) }
	var amountState by remember { mutableStateOf(TextFieldValue("")) }
	var categoryIdState by remember { mutableStateOf(TextFieldValue("")) }
	var dateState by remember { mutableStateOf(TextFieldValue("")) }
	var paymentMethodState by remember { mutableStateOf(TextFieldValue("")) }
	var bankAccountIdState by remember { mutableStateOf(TextFieldValue("")) }
	var creditCardIdState by remember { mutableStateOf(TextFieldValue("")) }
	var cashIdState by remember { mutableStateOf(TextFieldValue("")) }

	Surface(
		modifier = Modifier.fillMaxSize()
	) {
		Column {
			TextField(
				value = dateState,
				onValueChange = {
					dateState = it
				},
				label = { Text("Date") },
				modifier = Modifier
					.fillMaxWidth()
					.padding(start = 20.dp, end = 20.dp, bottom = 10.dp)
			)

			TextField(
				value = descriptionState,
				onValueChange = {
					descriptionState = it
				},
				label = { Text("Description") },
				modifier = Modifier
					.fillMaxWidth()
					.padding(start = 20.dp, end = 20.dp, bottom = 10.dp)
			)

			TextField(
				value = amountState,
				onValueChange = {
					amountState = it
				},
				label = { Text("Amount") },
				modifier = Modifier
					.fillMaxWidth()
					.padding(start = 20.dp, end = 20.dp, bottom = 10.dp)
			)

			TextField(
				value = categoryIdState,
				onValueChange = {
					categoryIdState = it
				},
				label = { Text("Category ID") },
				modifier = Modifier
					.fillMaxWidth()
					.padding(start = 20.dp, end = 20.dp, bottom = 10.dp)
			)



			TextField(
				value = paymentMethodState,
				onValueChange = {
					paymentMethodState = it
				},
				label = { Text("Payment Method") },
				modifier = Modifier
					.fillMaxWidth()
					.padding(start = 20.dp, end = 20.dp, bottom = 10.dp)
			)

			TextField(
				value = bankAccountIdState,
				onValueChange = {
					bankAccountIdState = it
				},
				label = { Text("Bank Account ID") },
				modifier = Modifier
					.fillMaxWidth()
					.padding(start = 20.dp, end = 20.dp, bottom = 10.dp)
			)

			TextField(
				value = creditCardIdState,
				onValueChange = {
					creditCardIdState = it
				},
				label = { Text("Credit Card ID") },
				modifier = Modifier
					.fillMaxWidth()
					.padding(start = 20.dp, end = 20.dp, bottom = 10.dp)
			)

			TextField(
				value = cashIdState,
				onValueChange = {
					cashIdState = it
				},
				label = { Text("Cash ID") },
				modifier = Modifier
					.fillMaxWidth()
					.padding(start = 20.dp, end = 20.dp, bottom = 10.dp)
			)

			Button(
				onClick = {
					// Handle the form submission here, e.g., save the expense
					val expense = Expense(
						description = descriptionState.text,
						amount = amountState.text.toDoubleOrNull() ?: 0.0,
						categoryId = categoryIdState.text.toIntOrNull(),
						date = Date(dateState.text),
						paymentMethod = paymentMethodState.text,
						bankAccountId = bankAccountIdState.text.toIntOrNull(),
						creditCardId = creditCardIdState.text.toIntOrNull(),
						cashId = cashIdState.text.toIntOrNull()
					)
					// Save the expense data

					// Navigate back to the previous screen
					navHostController.popBackStack()
				}
			) {
				Text("Save")
			}
		}
	}
}