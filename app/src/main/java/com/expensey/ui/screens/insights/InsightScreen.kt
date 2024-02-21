package com.expensey.ui.screens.insights

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.expensey.R
import com.expensey.data.models.Expense
import com.expensey.data.models.ExpenseSummary
import com.expensey.ui.screens.home.HomeViewModel
import com.expensey.ui.theme.Typography
import java.time.ZoneId
import java.util.Calendar

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun InsightsScreen(navController: NavHostController) {
	val TAG = "Insights Screen"

	val insightsViewModel: InsightsViewModel = viewModel()

	var selectedMonth by remember { mutableIntStateOf(Calendar.getInstance().get(Calendar.MONTH) + 1) }

	val expenseListFlow = insightsViewModel.expenseFlowList

	var expensesList : List<Expense> = expenseListFlow.collectAsState(initial = emptyList()).value

	var expenseListFilteredByMonth : List<Expense> = expensesList.filter {expense: Expense ->
		val expenseLocalDate = expense.date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
		val expenseMonth = expenseLocalDate.monthValue.toString()
		expenseMonth == selectedMonth.toString()
	}
	
	val mostSpentCategoryCountList : List<ExpenseSummary>? = insightsViewModel.mostSpentCategoryCountList.value
	if (mostSpentCategoryCountList != null) {
		Log.d(TAG, "InsightsScreen: Inside the if block")
		if(mostSpentCategoryCountList.isNotEmpty()) {
			Log.d(TAG, "InsightsScreen Most Spent Category: $mostSpentCategoryCountList")
		}
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