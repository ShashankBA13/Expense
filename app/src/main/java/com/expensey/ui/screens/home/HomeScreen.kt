package com.expensey.ui.screens.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.expensey.R
import com.expensey.data.models.BankAccount
import com.expensey.data.models.Category
import com.expensey.data.models.CreditCard
import com.expensey.data.models.Expense
import com.expensey.ui.screens.accounts.AccountsViewModel
import com.expensey.ui.screens.category.CategoryViewModel
import com.expensey.ui.theme.ExpenseyTheme
import com.expensey.ui.theme.Typography
import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import java.time.LocalDate
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

	val totalExpenseState : State<Double?> =
		viewModel.getTotalSumOfExpenses().collectAsState(initial = null)
	val totalExpense : Double? by totalExpenseState

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
					style = Typography.headlineMedium,
					color = MaterialTheme.colorScheme.primary
				)
				if (totalExpense != null) {
					Text(
						text = "₹ $totalExpense",
						textAlign = TextAlign.End,
						modifier = Modifier.padding(end = 20.dp),
						style = Typography.headlineLarge,
						color = MaterialTheme.colorScheme.primary,
						fontFamily = FontFamily(Font(R.font.archivo_black_regular))
					)
				}
			}

			if (groupedExpenses.isEmpty()) {
				Image(
					painter = painterResource(id = R.drawable.savings_money_bank_banking_woman),
					contentDescription = "Savings illustration",
					modifier = Modifier.height(500.dp)
				)
				Row  (
					modifier = Modifier.fillMaxWidth(),
					horizontalArrangement = Arrangement.Center
				){
					Text(
						text = "Start adding expenses by clicking the add button below",
						style = TextStyle(
							textAlign = TextAlign.Center,
							fontSize = 16.sp,
							color = MaterialTheme.colorScheme.primary
						)
					)
				}
			}

			LazyColumn(
				modifier = Modifier.weight(1f)
			) {
				groupedExpenses.forEach { (date, expenses) ->

					val today = LocalDate.now()
					val yesterday = today.minusDays(1)

					val displayDate = when {
						date.equals(today.format(DateTimeFormatter.ofPattern("dd MMM yyyy"))) -> "Today"
						date.equals(yesterday.format(DateTimeFormatter.ofPattern("dd MMM yyyy"))) -> "Yesterday"
						else -> date.format(DateTimeFormatter.ofPattern("dd MMM yyyy"))
					}

					var totalSpendsPerDay = 0.0
					expenses.forEach { expense ->
						totalSpendsPerDay += expense.amount
					}

					stickyHeader {
						Row(
							modifier = Modifier
								.fillMaxWidth()
								.padding(start = 20.dp, top = 20.dp, end = 20.dp),
							horizontalArrangement = Arrangement.SpaceBetween
						) {
							Text(
								text = displayDate,
								style = Typography.headlineSmall,
								textAlign = TextAlign.Start
							)
							Text(
								text = "₹ $totalSpendsPerDay",
								style = Typography.headlineSmall,
								color = MaterialTheme.colorScheme.primary,
								fontFamily = FontFamily(Font(R.font.archivo_black_regular))
							)
						}
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
					.padding(bottom = 90.dp, end = 40.dp)
					.align(Alignment.End)
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

	val accountsViewModel : AccountsViewModel = viewModel()
	val categoryViewModel : CategoryViewModel = viewModel()

	var bankAccount by remember { mutableStateOf<BankAccount?>(null) }
	var paymentMode = expense.paymentMethod

	if(expense.paymentMethod == "Bank Account") {
		val bankAccountLiveData = if (expense.paymentId != null && expense.paymentId != 0) {
			accountsViewModel.getBankAccountById(expense.paymentId !!)
		} else {
			null
		}
		bankAccountLiveData?.observeAsState()?.value?.let { bankAccount = it }
	}

	if (bankAccount != null) {
		paymentMode = bankAccount !!.accountName
	}

	var creditCard by remember {
		mutableStateOf<CreditCard?>(null)
	}

	if(expense.paymentMethod == "Credit Card") {
		val creditCardLiveData = if (expense.paymentId != null && expense.paymentId != 0) {
			accountsViewModel.fetchCreditCardById(expense.paymentId !!)
		} else {
			null
		}

		if (creditCardLiveData != null) {
			creditCardLiveData.observeAsState().value?.let { creditCard = it }
		}
	}

	if (creditCard != null) {
		paymentMode = creditCard !!.name
	}

	val categoryIdEdit = expense.categoryId
	val categoryFlow = if (categoryIdEdit != null) {
		categoryViewModel.getCategoryById(categoryIdEdit)
	} else {
		null
	}
	var category by remember { mutableStateOf<Category?>(null) }

	LaunchedEffect(categoryFlow) {
		categoryFlow?.collect { collectedCategory ->
			category = collectedCategory
		}
	}

	val categoryName = category?.categoryName

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
				.background(MaterialTheme.colorScheme.primaryContainer),
			horizontalArrangement = Arrangement.SpaceBetween,
			verticalAlignment = Alignment.CenterVertically
		) {

			Column(
				modifier = Modifier
					.padding(16.dp)
			) {
				Row(
					modifier = Modifier.fillMaxWidth(),
					horizontalArrangement = Arrangement.SpaceBetween
				) {
					Text(
						text = expense.description,
						style = Typography.bodyLarge,
						fontWeight = FontWeight.Bold,
						textAlign = TextAlign.Start
					)
					if (categoryName != null) {
						Text(
							text = categoryName,
							style = Typography.bodyLarge,
							fontWeight = FontWeight.Bold,
							textAlign = TextAlign.End
						)
					}
				}
				Spacer(modifier = Modifier.height(5.dp))
				Row(
					modifier = Modifier.fillMaxWidth(),
					horizontalArrangement = Arrangement.SpaceBetween
				) {
					Text(
						text = paymentMode,
						style = Typography.bodyMedium,
						textAlign = TextAlign.Start
					)
					Text(
						text = "₹ ${expense.amount}",
						style = Typography.headlineSmall,
						color = MaterialTheme.colorScheme.primary,
						textAlign = TextAlign.End,
						fontFamily = FontFamily(Font(R.font.archivo_black_regular)),
						fontWeight = FontWeight.Light
					)
				}
			}

		}
	}
}

private fun getGreeting() : String {
	val currentTime = Calendar.getInstance().time
	val hourFormat = SimpleDateFormat("HH", Locale.getDefault())
	val hour = hourFormat.format(currentTime).toInt()

	return when (hour) {
		in 6 .. 11 -> "Good Morning"
		in 12 .. 15 -> "Good Afternoon"
		in 16 .. 23, in 0 .. 5 -> "Good Evening"
		else -> "Hello" // Default greeting
	}
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun groupExpensesByDate(expenses : List<Expense>) : List<Pair<String, List<Expense>>> {
	val groupedExpenses = expenses.groupBy {
		DateTimeFormatter.ofPattern("dd MMM yyyy")
			.format(it.date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime())
	}
	return groupedExpenses.map { (date, expenses) ->
		date to expenses.sortedByDescending { it.date }
	}.sortedByDescending { it.first }
}