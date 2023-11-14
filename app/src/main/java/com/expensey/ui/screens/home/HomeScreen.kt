package com.expensey.ui.screens.home

import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.expensey.data.models.Expense
import com.expensey.ui.theme.ExpenseyTheme
import com.expensey.ui.theme.Typography
import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(navController : NavHostController) {

	val viewModel : HomeViewModel = viewModel()

	val expenseFlowList : Flow<List<Expense>> = viewModel.expenseFlowList
	val expenseList by expenseFlowList.collectAsState(initial = emptyList())

	val totalExpenseState: State<Double?> = viewModel.getTotalSumOfExpenses().collectAsState(initial = null)
	val totalExpense: Double? by totalExpenseState

	val greeting = getGreeting()

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
					text = greeting,
					modifier = Modifier
						.padding(20.dp),
					style = Typography.headlineMedium
				)
				Text(
					text = "₹ " + totalExpense.toString(),
					textAlign = TextAlign.End,
					modifier = Modifier.padding(end = 20.dp),
					style = Typography.headlineLarge
				)
			}

			LazyColumn(
				modifier = Modifier.weight(1f)
			) {
				items(expenseList) { expense ->
					ExpenseCard(expense = expense, navController)
				}
			}

			FloatingActionButton(
				onClick = {
					navController.navigate("expense/0")
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


@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun HomeScreenPreview() {
	val navController = rememberNavController()
	ExpenseyTheme {
		HomeScreen(navController)
	}
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ExpenseCard(expense : Expense, navController : NavHostController) {
	ElevatedCard(
		modifier = Modifier
			.fillMaxWidth()
			.padding(16.dp, top = 10.dp, end = 16.dp),
		elevation = CardDefaults.cardElevation(
			defaultElevation = 6.dp
		)
	) {
		Row(
			modifier = Modifier.clickable {
				navController.navigate("expense/${expense.expenseId}")
			} then Modifier
				.fillMaxWidth()
				.padding(16.dp),
			horizontalArrangement = Arrangement.SpaceBetween,
		) {
			Column {
				Text(
					text = expense.description,
					style = Typography.bodyLarge
				)
				Text(
					text = DateTimeFormatter.ofPattern("dd MMM yyyy | HH:mm").format(
						expense.date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()
					),
					style = Typography.bodySmall
				)
			}
			Text(
				text = "${expense.amount}",
				style = Typography.bodyLarge,
				textAlign = TextAlign.End
			)
		}
	}
}

private fun getGreeting(): String {
	val currentTime = Calendar.getInstance().time
	val hourFormat = SimpleDateFormat("HH", Locale.getDefault())
	val hour = hourFormat.format(currentTime).toInt()

	return when {
		hour in 6..11 -> "Good Morning"
		hour in 12..15 -> "Good Afternoon"
		hour in 16..23 || hour in 0..5 -> "Good Evening"
		else -> "Hello" // Default greeting
	}
}