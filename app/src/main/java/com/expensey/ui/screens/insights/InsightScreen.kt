package com.expensey.ui.screens.insights

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
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
	Surface {
		Column {
			Column(
				modifier = Modifier.fillMaxSize()
			) {
				Text(text = "Insights",
					style = Typography.headlineLarge,
					color = MaterialTheme.colorScheme.primary,
					fontFamily = FontFamily(Font(R.font.archivo_black_regular))
				)
				totalsProvider()
			}

		}

	}
}

@Composable 
fun totalsProvider() {
	val viewModel : HomeViewModel = viewModel()
	val insightsViewModel : InsightsViewModel = viewModel()
	val categoryViewModel : CategoryViewModel = viewModel()

	val expenseFlowList : Flow<List<Expense>> = viewModel.expenseFlowList
	val expenseList by expenseFlowList.collectAsState(initial = emptyList())


	val totalExpenseState : State<Double?> =
		viewModel.getTotalSumOfExpenses().collectAsState(initial = null)
	val totalExpense : Double? by totalExpenseState

	val mostSpentCategoryCountList: List<ExpenseSummary> = insightsViewModel.mostSpentCategoryCountList.toMutableList()

	Surface {
		Column {
			Text(text = "Total Spends till date: ${totalExpense?.toString()}")
			Text(text="Most Spent Category: ${mostSpentCategoryCountList.get(0).categoryName}")
			Text(text = "Amount Spent: ${mostSpentCategoryCountList.get(0).amountSpent}")
		}
	}

}

