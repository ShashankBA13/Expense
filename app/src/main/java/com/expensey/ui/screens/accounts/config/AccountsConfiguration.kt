package com.expensey.ui.screens.accounts.config

import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.ArrowBackIos
import androidx.compose.material.icons.outlined.ArrowForwardIos
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.expensey.data.models.BankAccount
import com.expensey.ui.screens.accounts.AccountsViewModel
import com.expensey.ui.theme.Typography

@Composable
fun AccountsConfiguration(navController : NavHostController) {

	val viewModel: AccountsViewModel = viewModel()

	Surface(
		modifier = Modifier.fillMaxSize()
	) {
		Column {
			Row(
				modifier = Modifier.fillMaxWidth(),
				horizontalArrangement = Arrangement.SpaceBetween,
				verticalAlignment = Alignment.CenterVertically
			) {
				Icon(
					imageVector = Icons.Outlined.ArrowBackIos,
					contentDescription = "Back", // Provide a content description as needed
					modifier = Modifier.clickable {
						navController.popBackStack()
					} then Modifier.padding(20.dp, end = 0.dp)
				)

				Text(
					text = "Accounts",
					modifier = Modifier
						.padding(20.dp)
						.weight(1f),
					style = Typography.headlineLarge
				)
				Icon(
					imageVector = Icons.Outlined.Add,
					contentDescription = "Add Icon", // Provide a content description as needed
					modifier = Modifier.clickable {
						// Handle the icon click here
					} then Modifier
						.padding(end = 20.dp)
				)
			}

			Cash(navController)
			Account(navController)
			CreditCard(navController)
		}
	}
}

@Composable
fun Cash(navController : NavHostController) {

	Column {
		Row (
			modifier = Modifier
				.height(100.dp)
				.fillMaxWidth()
				.padding(20.dp)
				.border(1.dp, Color.Gray, shape = RoundedCornerShape(5.dp)),
			horizontalArrangement = Arrangement.SpaceBetween,
			verticalAlignment = Alignment.CenterVertically
		) {
			Text (
				text = "Cash",
				modifier = Modifier.padding(10.dp)
			)
			Icon(
				imageVector = Icons.Outlined.ArrowForwardIos,
				contentDescription = "Go to Cash",
				modifier = Modifier.clickable {
					navController.navigate("cash")
				} then Modifier.padding(end = 10.dp)
			)
		}
	}
}

@Composable
fun Account(navController : NavHostController) {

	val TAG = "AccountsConfiguration"

	val viewModel: AccountsViewModel = viewModel()

	val bankAccountLiveDataList : LiveData<List<BankAccount>> = viewModel.bankAccountLiveDataList

	val bankAccounts by bankAccountLiveDataList.observeAsState(emptyList())

	bankAccounts.forEach { bankAccount ->
		Log.d(TAG, "BankAccountName: ${bankAccount.accountName}")
	}

	Column {
		Row (
			modifier = Modifier
				.height(100.dp)
				.fillMaxWidth()
				.padding(20.dp)
				.border(1.dp, Color.Gray, shape = RoundedCornerShape(5.dp)),
			horizontalArrangement = Arrangement.SpaceBetween,
			verticalAlignment = Alignment.CenterVertically
		) {
			Text (
				text = "Accounts",
				modifier = Modifier.padding(10.dp)
			)
		}

		Column {
			bankAccounts.forEach{bankAccount ->
				Text(
					text = bankAccount.accountName,
					modifier = Modifier.padding(10.dp)
				)

				Icon(
					imageVector = Icons.Outlined.ArrowForwardIos,
					contentDescription = "Go to Accounts",
					modifier = Modifier.clickable {
						navController.navigate("bankAccount")
					} then Modifier.padding(end = 10.dp)
				)
			}
		}
	}
}

@Composable
fun CreditCard(navController : NavHostController) {
	Column {
		Row (
			modifier = Modifier
				.height(100.dp)
				.fillMaxWidth()
				.padding(20.dp)
				.border(1.dp, Color.Gray, shape = RoundedCornerShape(5.dp)),
			horizontalArrangement = Arrangement.SpaceBetween,
			verticalAlignment = Alignment.CenterVertically
		) {
			Text (
				text = "Credit Cards",
				modifier = Modifier.padding(10.dp)
			)
			Icon(
				imageVector = Icons.Outlined.ArrowForwardIos,
				contentDescription = "Go to Credit Card",
				modifier = Modifier.clickable {
					navController.navigate("creditCard")
				} then Modifier.padding(end = 10.dp)
			)
		}
	}
}