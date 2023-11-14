package com.expensey.ui.screens.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
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

@OptIn(ExperimentalFoundationApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(navController : NavHostController) {

	val viewModel : HomeViewModel = viewModel()

	val expenseFlowList : Flow<List<Expense>> = viewModel.expenseFlowList
	val expenseList by expenseFlowList.collectAsState(initial = emptyList())

	val totalExpenseState: State<Double?> = viewModel.getTotalSumOfExpenses().collectAsState(initial = null)
	val totalExpense: Double? by totalExpenseState

	val greeting = getGreeting()

	val groupedExpenses = groupExpensesByDate(expenseList)

	Surface(
		modifier = Modifier.fillMaxSize(),
		color = MaterialTheme.colorScheme.background
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
					style = Typography.headlineLarge,
					color = MaterialTheme.colorScheme.primary
				)
			}

			LazyColumn(
				modifier = Modifier.weight(1f)
			) {
				groupedExpenses.forEach { (date, expenses) ->
					stickyHeader {
						Text(
							text = date,
							style = Typography.headlineSmall,
							modifier = Modifier
								.fillMaxWidth()
								.padding(start = 20.dp, top = 20.dp)
						)
					}
					items(expenses) { expense ->
						ExpenseCard(expense = expense, navController)
					}
				}
			}

			FloatingActionButton(
				onClick = {
					navController.navigate("expense/0")
				},
				modifier = Modifier
					.padding(bottom = 90.dp)
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
fun ExpenseCard(expense: Expense, navController: NavHostController) {
	Card(
		modifier = Modifier
			.fillMaxWidth()
			.padding(16.dp, top = 10.dp, end = 16.dp)
			.clickable { navController.navigate("expense/${expense.expenseId}") },
	) {
		Row(
			modifier = Modifier
				.fillMaxWidth()
				.height(70.dp)
				.background(MaterialTheme.colorScheme.secondaryContainer),
			horizontalArrangement = Arrangement.SpaceBetween,
			verticalAlignment = Alignment.CenterVertically
		) {

			Column(
				modifier = Modifier
					.padding(16.dp)
					.weight(1f)
			) {
				Text(
					text = expense.description,
					style = Typography.bodyLarge,
					fontWeight = FontWeight.Bold
				)
				Spacer(modifier = Modifier.height(4.dp))
				Text(
					text = buildAnnotatedString {
						withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
							append("Mode: ")
						}
						append(expense.paymentMethod)
					},
					style = Typography.bodyMedium,
					color = MaterialTheme.colorScheme.tertiary
				)
			}
			Text(
				text = buildAnnotatedString {
					withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
					}
					append("₹${expense.amount}")
				},
				style = Typography.bodyLarge,
				textAlign = TextAlign.Center,
				modifier = Modifier
					.padding(end = 16.dp, top = 15.dp)
					.align(Alignment.CenterVertically), // Center the text vertically
				color = MaterialTheme.colorScheme.primary
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

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun groupExpensesByDate(expenses: List<Expense>): List<Pair<String, List<Expense>>> {
	val groupedExpenses = expenses.groupBy {
		DateTimeFormatter.ofPattern("dd MMM yyyy").format(it.date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime())
	}
	return groupedExpenses.map { (date, expenses) ->
		date to expenses.sortedByDescending { it.date }
	}.sortedByDescending { it.first }
}