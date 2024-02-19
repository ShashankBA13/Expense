package com.expensey.ui.screens.insights

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Money
import androidx.compose.material.icons.outlined.Wallet
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.expensey.R
import com.expensey.data.models.Expense
import com.expensey.ui.screens.home.HomeViewModel
import com.expensey.ui.theme.Typography
import java.util.Calendar

@Composable
fun InsightsScreen(navController: NavHostController) {
	val TAG = "Insights Screen"

	val insightsViewModel: InsightsViewModel = viewModel()

	var selectedMonth by remember { mutableIntStateOf(Calendar.getInstance().get(Calendar.MONTH) + 1) }

	var expenseListFlow = insightsViewModel.expenseFlowList

	var expensesList : List<Expense> = emptyList()
	expensesList = expenseListFlow.collectAsState(initial = emptyList()).value!!

	var expenseListFilteredByMonth : List<Expense> = expensesList.filter {expense: Expense ->
		val expenseMonth = (expense.date.month + 1).toString()
		expenseMonth == selectedMonth.toString()
	}

	Surface(
		modifier = Modifier.fillMaxSize(),
		color = MaterialTheme.colorScheme.background
	) {
		Column(
			modifier = Modifier
				.fillMaxSize()
				.padding(16.dp),
			horizontalAlignment = Alignment.CenterHorizontally
		) {
			Text(
				text = "Insights",
				style = Typography.headlineLarge,
				color = MaterialTheme.colorScheme.primary,
				fontFamily = FontFamily(Font(R.font.archivo_black_regular))
			)
			Spacer(modifier = Modifier.height(16.dp))
			TotalsProvider()

			// Check if expenses are empty or not and display accordingly
			if (expenseListFilteredByMonth.isEmpty()) {
				Text(
					text = "No expenses available",
					style = Typography.bodyLarge,
					color = MaterialTheme.colorScheme.primary
				)
			} else {
				val totalExpensesForTheMonth = expenseListFilteredByMonth.sumOf { it.amount }
				Text(
					text = "Total Expenses this month: $totalExpensesForTheMonth",
					style = Typography.headlineLarge,
					color = MaterialTheme.colorScheme.primary
				)
			}
		}
	}
}

@Composable
fun TotalsProvider() {
	val viewModel: HomeViewModel = viewModel()
	val insightsViewModel: InsightsViewModel = viewModel()

	val totalExpense by viewModel.getTotalSumOfExpenses().collectAsState(initial = null)
	val mostSpentCategoryCountList by insightsViewModel.mostSpentCategoryCountList.observeAsState()

	Column(
		modifier = Modifier.fillMaxWidth(),
		horizontalAlignment = Alignment.CenterHorizontally
	) {
		TotalCard(icon = Icons.Outlined.Wallet, label = "Total Spends", value = totalExpense?.toString())
		Spacer(modifier = Modifier.height(16.dp))
		mostSpentCategoryCountList?.let {
			if (it.isNotEmpty()) {
				TotalCard(icon = Icons.Default.Category, label = "Most Spent Category", value = it[0].categoryName)
				Spacer(modifier = Modifier.height(8.dp))
				TotalCard(icon = Icons.Default.Money, label = "Amount Spent", value = it[0].amountSpent.toString())
			}
		}
	}
}

@Composable
fun TotalCard(icon: ImageVector, label: String, value: String?) {
	Card(
		modifier = Modifier
			.fillMaxWidth()
			.padding(16.dp)
			.clickable {}
	) {
		Column(
			modifier = Modifier
				.fillMaxWidth()
				.padding(16.dp),
			horizontalAlignment = Alignment.CenterHorizontally
		) {
			Icon(imageVector = icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
			Spacer(modifier = Modifier.height(8.dp))
			Text(text = label, style = MaterialTheme.typography.labelLarge)
			Spacer(modifier = Modifier.height(4.dp))
			Text(
				text = value ?: "-",
				style = MaterialTheme.typography.bodySmall,
				color = MaterialTheme.colorScheme.primary,
				fontSize = 20.sp
			)
		}
	}
}

@Preview
@Composable
fun InsightsScreenPreview() {
	InsightsScreen(navController = rememberNavController())
}