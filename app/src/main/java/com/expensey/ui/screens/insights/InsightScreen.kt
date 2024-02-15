package com.expensey.ui.screens.insights

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Money
import androidx.compose.material.icons.outlined.FireTruck
import androidx.compose.material.icons.outlined.Wallet
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.expensey.R
import com.expensey.data.models.Category
import com.expensey.data.models.CategoryCount
import com.expensey.data.models.Expense
import com.expensey.data.models.ExpenseSummary
import com.expensey.ui.screens.category.CategoryViewModel
import com.expensey.ui.screens.home.HomeViewModel
import com.expensey.ui.theme.Typography
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

@Composable
fun InsightsScreen(navController: NavHostController) {

	val insightsViewModel: InsightsViewModel = viewModel()

	val expensesState = remember { mutableStateOf<List<Expense>>(emptyList()) }
	// Create a MutableState variable to hold the selected month
	var selectedMonth by remember { mutableStateOf("01") }

	LaunchedEffect(key1 = insightsViewModel) {
		val expenses = insightsViewModel.getSpendsByMonth(selectedMonth)
		expensesState.value = expenses
	}

	val expenses = expensesState.value

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
			.padding(16.dp).clickable {}
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