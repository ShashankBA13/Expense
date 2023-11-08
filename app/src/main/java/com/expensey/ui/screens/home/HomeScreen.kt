package com.expensey.ui.screens.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.expensey.data.models.Expense
import com.expensey.ui.theme.ExpenseyTheme
import com.expensey.ui.theme.Typography
import kotlinx.coroutines.flow.Flow

@Composable
fun HomeScreen(navController : NavHostController) {

	val viewModel : HomeViewModel = viewModel()

	val expenseFlowList : Flow<List<Expense>> = viewModel.expenseFlowList
	val expenseList by expenseFlowList.collectAsState(initial = emptyList())

	Surface(
		modifier = Modifier.fillMaxSize()
	) {
		Column(
			modifier = Modifier.fillMaxSize()
		) {
			Row(
				modifier = Modifier.fillMaxWidth(),
				horizontalArrangement = Arrangement.SpaceBetween,
				verticalAlignment = Alignment.CenterVertically
			) {
				Text(
					text = "Home",
					modifier = Modifier
						.padding(20.dp),
					style = Typography.headlineLarge
				)
			}

			LazyColumn(
				modifier = Modifier.weight(1f)
			) {
				items(expenseList) { expense ->
					ExpenseCard(expense = expense)
				}
			}

			FloatingActionButton(
				onClick = {
					navController.navigate("expense")
				},
				modifier = Modifier
					.padding(bottom = 100.dp)
                    .align(Alignment.CenterHorizontally)
			) {
				Icon(Icons.Filled.Add, "Floating action button.")
			}
		}
	}
}


@Preview
@Composable
fun HomeScreenPreview() {
	val navController = rememberNavController()
	ExpenseyTheme {
		HomeScreen(navController)
	}
}

@Composable
fun ExpenseCard(expense : Expense) {
	ElevatedCard(
		modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp, top = 10.dp, end = 16.dp),
		elevation = CardDefaults.cardElevation(
			defaultElevation = 6.dp
		)
	) {
		Column(
			modifier = Modifier.clickable {

			} then Modifier
                .fillMaxWidth()
                .padding(16.dp)
		) {
			Text(
				text = expense.description,
				style = Typography.bodyLarge
			)
			Text(
				text = "Amount: ${expense.amount}",
				style = Typography.bodySmall
			)
		}
	}
}
