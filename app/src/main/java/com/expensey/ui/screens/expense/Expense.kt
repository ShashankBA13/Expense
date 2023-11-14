package com.expensey.ui.screens.expense

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.outlined.ArrowBackIos
import androidx.compose.material.icons.outlined.AttachMoney
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.expensey.data.models.Category
import com.expensey.data.models.Expense
import com.expensey.ui.screens.category.CategoryViewModel
import com.expensey.ui.screens.home.HomeViewModel
import com.expensey.ui.theme.Typography
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

@SuppressLint("RememberReturnType")
@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseScreen(navHostController : NavHostController, expenseId : Int) {
	val TAG = "Expense"

	val context = LocalContext.current
	val homeViewModel: HomeViewModel = viewModel()
	val categoryViewModel: CategoryViewModel = viewModel()

	val expenseFlow = if (expenseId != 0) {
		homeViewModel.getExpenseById(expenseId)
	} else {
		null
	}

	var expense by remember { mutableStateOf<Expense?>(null) }

	LaunchedEffect(expenseFlow) {
		expenseFlow?.collect { collectedExpense ->
			expense = collectedExpense
		}
	}

	val pickedDate by remember { mutableStateOf(LocalDateTime.now()) }
	var descriptionState by remember { mutableStateOf(TextFieldValue("")) }
	var amountState by remember { mutableStateOf(TextFieldValue("")) }
	var categoryId by remember { mutableStateOf(0) }
	var dateState by remember { mutableStateOf(TextFieldValue("")) }
	dateState = remember {
		val formattedDate = pickedDate.format(DateTimeFormatter.ofPattern("dd MMM yyyy | HH:mm"))
		TextFieldValue(formattedDate)
	}

	if(expense != null) {
		dateState = TextFieldValue(DateTimeFormatter.ofPattern("dd MMM yyyy | HH:mm").format(
			expense !!.date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()
		))
		amountState = TextFieldValue(expense?.amount.toString())
		descriptionState = TextFieldValue(expense?.description.toString())
	}

	var paymentMethodState by remember { mutableStateOf(TextFieldValue("")) }
	var bankAccountIdState by remember { mutableStateOf(TextFieldValue("")) }
	var creditCardIdState by remember { mutableStateOf(TextFieldValue("")) }
	var cashIdState by remember { mutableStateOf(TextFieldValue("")) }

	var isCategoryMenuExpanded by remember { mutableStateOf(false) }
	val categoryListState by categoryViewModel.categoryLiveDataList.observeAsState()
	val categories = categoryListState ?: emptyList()
	var selectedCategory by remember { mutableStateOf<Category?>(null) }

	Surface(modifier = Modifier.fillMaxSize()) {
		Column {
			Row(modifier = Modifier.fillMaxWidth()) {
				Icon(
					imageVector = Icons.Outlined.ArrowBackIos,
					contentDescription = "Back",
					modifier = Modifier.clickable {
						navHostController.popBackStack()
					} then Modifier.padding(start = 20.dp, top = 20.dp, end = 10.dp)
				)

				Text(
					text = "Expense",
					modifier = Modifier
						.padding(20.dp)
						.weight(1f),
					style = Typography.headlineLarge
				)
			}

			Column(modifier = Modifier.fillMaxWidth()) {
				OutlinedTextField(
					value = dateState,
					onValueChange = {
						dateState = it
					},
					label = { Text("Date") },
					modifier = Modifier
						.fillMaxWidth()
						.padding(start = 20.dp, end = 20.dp, bottom = 10.dp),
					trailingIcon = {
						IconButton(
							onClick = {
								showDateTimePicker(context)
								{
									selectedDateTime ->
									dateState = TextFieldValue(selectedDateTime.format(DateTimeFormatter.ofPattern("dd MMM yyyy | HH:mm")))
								}
							}) {
								Icon(
									imageVector = Icons.Default.CalendarToday,
									contentDescription = "Date And Time"
								)
							}
					}
				)

				OutlinedTextField(
					value = amountState,
					onValueChange = {
						amountState = it
					},
					label = { Text("Amount") },
					modifier = Modifier
						.fillMaxWidth()
						.padding(start = 20.dp, end = 20.dp, bottom = 10.dp),
					trailingIcon = {
						Icon(
							Icons.Outlined.AttachMoney,
							contentDescription = "Amount"
						)
					}
				)

				OutlinedTextField(
					value = descriptionState,
					onValueChange = {
						descriptionState = it
					},
					label = { Text("Description") },
					modifier = Modifier
						.fillMaxWidth()
						.padding(start = 20.dp, end = 20.dp, bottom = 10.dp),
					trailingIcon = {
						Icon(
							Icons.Outlined.Description,
							contentDescription = "Description Icon"
						)
					}
				)

				OutlinedTextField(
					value = paymentMethodState,
					onValueChange = {
						paymentMethodState = it
					},
					label = { Text("Payment Method") },
					modifier = Modifier
						.fillMaxWidth()
						.padding(start = 20.dp, end = 20.dp, bottom = 10.dp)
				)

				ExposedDropdownMenuBox(
					expanded = isCategoryMenuExpanded,
					onExpandedChange = {
						isCategoryMenuExpanded = !isCategoryMenuExpanded
					},
					modifier = Modifier
						.padding(start = 20.dp, end = 20.dp, bottom = 10.dp)
						.fillMaxWidth()
				) {
					OutlinedTextField(
						value = selectedCategory?.categoryName ?: "",
						onValueChange = {},
						readOnly = true,
						label = { Text("Category") },
						trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isCategoryMenuExpanded) },
						modifier = Modifier
							.fillMaxWidth()
							.clickable {
								isCategoryMenuExpanded = ! isCategoryMenuExpanded
							}
							.menuAnchor(),
						colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
					)

					ExposedDropdownMenu(
						expanded = isCategoryMenuExpanded,
						onDismissRequest = {
							isCategoryMenuExpanded = false
						},
						modifier = Modifier.fillMaxWidth()
					) {
						categories.forEach { category ->
							DropdownMenuItem(
								onClick = {
									categoryId = category.categoryId
									selectedCategory = category
									isCategoryMenuExpanded = false
								},
								text = { Text(category.categoryName) }
							)
						}
					}
				}

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
							val selectedDateTime = LocalDateTime.parse(
								dateState.text,
								DateTimeFormatter.ofPattern("dd MMM yyyy | HH:mm")
							)
							val selectedDate = Date.from(selectedDateTime.atZone(ZoneId.systemDefault()).toInstant())


							if(expenseId != null && expenseId != 0) {
								val updateExpense = Expense(
									expenseId = expenseId,
									description = descriptionState.text,
									amount = amountState.text.toDoubleOrNull() ?: 0.0,
									categoryId = categoryId,
									date = selectedDate,
									paymentMethod = paymentMethodState.text,
									bankAccountId = bankAccountIdState.text.toIntOrNull(),
									creditCardId = creditCardIdState.text.toIntOrNull(),
									cashId = cashIdState.text.toIntOrNull()
								)
								homeViewModel.updateExpense(expense = updateExpense)
							} else {
								val newExpense = Expense(
									description = descriptionState.text,
									amount = amountState.text.toDoubleOrNull() ?: 0.0,
									categoryId = categoryId,
									date = selectedDate,
									paymentMethod = paymentMethodState.text,
									bankAccountId = bankAccountIdState.text.toIntOrNull(),
									creditCardId = creditCardIdState.text.toIntOrNull(),
									cashId = cashIdState.text.toIntOrNull()
								)
								homeViewModel.insertExpense(newExpense)
							}
							navHostController.popBackStack()
						},
						modifier = Modifier.weight(1f)
					) {
						Text(if(expenseId != 0) "Update" else "Save")
					}
				}
			}
		}
	}
}

@RequiresApi(Build.VERSION_CODES.O)
fun showDateTimePicker(context: Context, onDateSelected: (LocalDateTime) -> Unit) {
	val currentDateTime: Calendar = Calendar.getInstance()
	val year: Int = currentDateTime.get(Calendar.YEAR)
	val month: Int = currentDateTime.get(Calendar.MONTH)
	val day: Int = currentDateTime.get(Calendar.DAY_OF_MONTH)

	// Date Picker Dialog
	val datePickerDialog = DatePickerDialog(
		context,
		{ _, selectedYear, selectedMonth, selectedDay ->
			currentDateTime.set(Calendar.YEAR, selectedYear)
			currentDateTime.set(Calendar.MONTH, selectedMonth)
			currentDateTime.set(Calendar.DAY_OF_MONTH, selectedDay)
			val hour: Int = currentDateTime.get(Calendar.HOUR_OF_DAY)
			val minute: Int = currentDateTime.get(Calendar.MINUTE)

			// Time Picker Dialog
			val timePickerDialog = TimePickerDialog(
				context,
				{ _, selectedHour, selectedMinute ->
					currentDateTime.set(Calendar.HOUR_OF_DAY, selectedHour)
					currentDateTime.set(Calendar.MINUTE, selectedMinute)

					// Format the selected date and time
					val selectedDateTime = LocalDateTime.ofInstant(currentDateTime.toInstant(), ZoneId.systemDefault())
					val dateFormat = DateTimeFormatter.ofPattern("dd MMM yyyy | HH:mm", Locale.getDefault())

					// Update the dateState value
					onDateSelected(selectedDateTime)
				},
				hour,
				minute,
				false
			)
			timePickerDialog.show()
		},
		year,
		month,
		day
	)
	datePickerDialog.show()
}