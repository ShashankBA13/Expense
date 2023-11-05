package com.expensey.ui.screens.accounts.config.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.expensey.data.models.Cash
import com.expensey.ui.screens.accounts.AccountsViewModel
import com.expensey.ui.theme.Typography

@Composable
fun CashScreen(navHostController: NavHostController) {

	val viewModel: AccountsViewModel = viewModel()
	val cashLiveData by viewModel.cashLiveData.observeAsState()

	var text by remember { mutableStateOf(cashLiveData?.amount?.toString() ?: "") }
	if (cashLiveData != null) {
		text = cashLiveData !!.amount.toString()
	}

	Surface(
		modifier = Modifier.fillMaxSize()
	) {
		Column {
			Row(
				modifier = Modifier
					.fillMaxWidth()
					.padding(start = 20.dp, top = 20.dp)
			) {
				Text(
					text = "Cash",
					style = Typography.titleLarge
				)
			}

			TextField(
				value = text,
				onValueChange = { text = it },
				label = { Text("Current Balance") },
				modifier = Modifier
					.fillMaxWidth()
					.padding(20.dp)
			)

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
						if (cashLiveData?.cashId != null) {
							cashLiveData!!.amount = text.toDouble()
							viewModel.updateCash(cashLiveData!!)
						} else {
							viewModel.insertCash(Cash(name = "Cash", amount = text.toDouble()))
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
