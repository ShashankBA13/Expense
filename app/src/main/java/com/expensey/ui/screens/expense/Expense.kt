package com.expensey.ui.screens.expense

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.outlined.ArrowBackIosNew
import androidx.compose.material.icons.outlined.AttachMoney
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.expensey.data.models.BankAccount
import com.expensey.data.models.Cash
import com.expensey.data.models.Category
import com.expensey.data.models.CreditCard
import com.expensey.data.models.Expense
import com.expensey.ui.screens.accounts.AccountsViewModel
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
fun ExpenseScreen(navHostController: NavHostController, expenseId: Int) {

    val context = LocalContext.current
    val homeViewModel: HomeViewModel = viewModel()
    val categoryViewModel: CategoryViewModel = viewModel()
    val expenseViewModel: ExpenseViewModel = viewModel()
    val accountsViewModel: AccountsViewModel = viewModel()

    var bankAccount by remember { mutableStateOf<BankAccount?>(null) }
    var creditCard by remember {
        mutableStateOf<CreditCard?>(null)
    }

    val cashLiveData by expenseViewModel.cashLiveData.observeAsState()
    val bankAccountLiveDataList by expenseViewModel.bankAccountLiveDataList.observeAsState()
    val creditCardLiveDataList by expenseViewModel.creditCardLiveDataList.observeAsState()

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
    var dateState by remember { mutableStateOf(TextFieldValue("")) }
    dateState = remember {
        val formattedDate = pickedDate.format(DateTimeFormatter.ofPattern("dd MMM yyyy | HH:mm"))
        TextFieldValue(formattedDate)
    }
    var descriptionState by remember { mutableStateOf(TextFieldValue("")) }
    var amountState by remember { mutableStateOf(TextFieldValue("")) }
    var categoryId by remember { mutableIntStateOf(0) }
    var paymentMethodState by remember { mutableStateOf(TextFieldValue("")) }
    var cashIdState by remember { mutableStateOf(TextFieldValue("")) }
    var bankAccountIdState by remember { mutableStateOf(TextFieldValue("")) }
    var creditCardIdState by remember { mutableStateOf(TextFieldValue("")) }


    var isCategoryMenuExpanded by remember { mutableStateOf(false) }
    val categoryListState by categoryViewModel.categoryLiveDataList.observeAsState()
    val categories = categoryListState ?: emptyList()
    var selectedCategory by remember { mutableStateOf<Category?>(null) }


    var isPaymentModeExpanded by remember { mutableStateOf(false) }
    val bankAccountsList = bankAccountLiveDataList ?: emptyList()
    val creditCardsList = creditCardLiveDataList ?: emptyList()
    var selectedPaymentMode by remember { mutableStateOf("") }


    if (expense != null) {
        dateState = TextFieldValue(
            DateTimeFormatter.ofPattern("dd MMM yyyy | HH:mm").format(
                expense!!.date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()
            )
        )
        amountState = TextFieldValue(expense!!.amount.toString())
        descriptionState = TextFieldValue(expense!!.description)

        val categoryIdEdit = expense!!.categoryId
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

        if (categoryIdEdit != null) {
            selectedCategory = category
            categoryId = categoryIdEdit
        }

        LaunchedEffect(expense!!.bankAccountId) {
            bankAccountIdState = TextFieldValue(expense!!.bankAccountId.toString())
            paymentMethodState = TextFieldValue("Bank Account")
        }

        val bankAccountIdInt: Int? = bankAccountIdState.text.toIntOrNull()

        if (bankAccountIdInt != null) {
            val bankAccountLiveData = if (bankAccountIdInt != 0) {
                accountsViewModel.getBankAccountById(bankAccountIdInt)
            } else {
                null
            }

            if (bankAccountLiveData != null) {
                bankAccountLiveData.observeAsState().value?.let { bankAccount = it }
            }

            if (bankAccount != null) {
                selectedPaymentMode =
                    selectedPaymentMode.takeIf { it.isNotBlank() } ?: bankAccount!!.accountName
            }
        }

        // Update cashIdState using LaunchedEffect
        LaunchedEffect(expense!!.cashId) {
            cashIdState = TextFieldValue(expense!!.cashId.toString())
            paymentMethodState = TextFieldValue("Cash")
        }

        val cashIdInt: Int? = cashIdState.text.toIntOrNull()

        if (cashIdInt != null && cashIdInt != 0) {
            selectedPaymentMode =
                (selectedPaymentMode.takeIf { it.isNotBlank() } ?: cashLiveData?.name).toString()
        }

        // Update cashIdState using LaunchedEffect
        LaunchedEffect(expense!!.creditCardId) {
            creditCardIdState = TextFieldValue(expense!!.creditCardId.toString())
            paymentMethodState = TextFieldValue("Credit Card")
        }

        val creditCardIdInt: Int? = creditCardIdState.text.toIntOrNull()

        if (creditCardIdInt != null && creditCardIdInt != 0) {
            val creditCardLiveData = if (creditCardIdInt != 0) {
                accountsViewModel.fetchCreditCardById(creditCardIdInt)
            } else {
                null
            }

            if (creditCardLiveData != null) {
                creditCardLiveData.observeAsState().value?.let { creditCard = it }
            }

            if (creditCard != null) {
                selectedPaymentMode =
                    selectedPaymentMode.takeIf { it.isNotBlank() } ?: creditCard!!.name
            }
        }
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        Column {
            Row(modifier = Modifier.fillMaxWidth()) {
                Icon(
                    imageVector = Icons.Outlined.ArrowBackIosNew,
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

                if (expenseId != 0) {
                    Icon(
                        imageVector = Icons.Outlined.Delete,
                        contentDescription = "Delete Bank Account",
                        modifier = Modifier.clickable {
                            expense?.let { homeViewModel.deleteExpense(it) }
                            Toast.makeText(context, "Expense Deleted", Toast.LENGTH_SHORT).show()
                            navHostController.popBackStack()
                        } then Modifier.padding(start = 20.dp, top = 20.dp, end = 20.dp),
                        tint = Color.Red
                    )
                }
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
                                { selectedDateTime ->
                                    dateState = TextFieldValue(
                                        selectedDateTime.format(
                                            DateTimeFormatter.ofPattern("dd MMM yyyy | HH:mm")
                                        )
                                    )
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
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number
                    )
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
								isCategoryMenuExpanded = !isCategoryMenuExpanded
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
                                text = { Text(category.categoryName) },
                                modifier = Modifier
                                    .fillMaxWidth()
                            )
                        }
                    }
                }

                ExposedDropdownMenuBox(
                    expanded = isPaymentModeExpanded,
                    onExpandedChange = {
                        isPaymentModeExpanded = !isPaymentModeExpanded
                    },
                    modifier = Modifier
						.padding(start = 20.dp, end = 20.dp, bottom = 10.dp)
						.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = selectedPaymentMode,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Payment Mode") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isPaymentModeExpanded) },
                        modifier = Modifier
							.fillMaxWidth()
							.clickable {
								isPaymentModeExpanded = !isPaymentModeExpanded
							}
							.menuAnchor(),
                        colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
                    )

                    ExposedDropdownMenu(
                        expanded = isPaymentModeExpanded,
                        onDismissRequest = {
                            isPaymentModeExpanded = false
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {

                        if (bankAccountsList.isNotEmpty()) {
                            DropdownMenuItem(
                                onClick = {
                                },
                                text = {
                                    Text("Bank Account", fontWeight = FontWeight.Bold)
                                }
                            )

                            Divider(modifier = Modifier.padding(start = 10.dp, end = 10.dp))
                        }

                        bankAccountsList.forEach { bankAccount ->
                            DropdownMenuItem(
                                onClick = {
                                    bankAccountIdState =
                                        TextFieldValue(bankAccount.accountId.toString())
                                    selectedPaymentMode = bankAccount.accountName
                                    isPaymentModeExpanded = false
                                    paymentMethodState = TextFieldValue("Bank Account")
                                },
                                modifier = Modifier.fillMaxWidth(),
                                text = {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = "${bankAccount.accountName}       ",
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = "₹ ${bankAccount.currentBalance}",
                                            fontWeight = FontWeight.Bold,
                                            color = Color.Gray // Adjust color based on your design
                                        )
                                    }
                                }
                            )
                        }

                        if (creditCardsList.isNotEmpty()) {
                            DropdownMenuItem(
                                onClick = {
                                },
                                text = {
                                    Text("Credit Card", fontWeight = FontWeight.Bold)
                                }
                            )

                            Divider(modifier = Modifier.padding(start = 10.dp, end = 10.dp))
                        }

                        creditCardsList.forEach { creditCard ->
                            DropdownMenuItem(
                                onClick = {
                                    creditCardIdState =
                                        TextFieldValue(creditCard.creditCardId.toString())
                                    selectedPaymentMode = creditCard.name
                                    isPaymentModeExpanded = false
                                    paymentMethodState = TextFieldValue("Credit Card")
                                },
                                modifier = Modifier.fillMaxWidth(),
                                text = {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = creditCard.name,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = "₹ ${creditCard.currentBalance}",
                                            fontWeight = FontWeight.Bold,
                                            color = Color.Gray
                                        )
                                    }
                                }
                            )
                        }

                        DropdownMenuItem(
                            onClick = {
                            },
                            text = {
                                Text("Cash", fontWeight = FontWeight.Bold)
                            }
                        )
                        Divider(modifier = Modifier.padding(start = 10.dp, end = 10.dp))

                        DropdownMenuItem(
                            onClick = {
                                cashIdState = TextFieldValue(cashLiveData?.cashId.toString())
                                selectedPaymentMode = cashLiveData?.name.toString()
                                isPaymentModeExpanded = false
                                paymentMethodState = TextFieldValue("Cash")
                            },
                            modifier = Modifier.fillMaxWidth(),
                            text = {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "${cashLiveData?.name}",
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "₹ ${cashLiveData?.amount}",
                                        fontWeight = FontWeight.Bold,
                                        color = Color.Gray // Adjust color based on your design
                                    )
                                }
                            }
                        )
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
                            val selectedDate = Date.from(
                                selectedDateTime.atZone(ZoneId.systemDefault()).toInstant()
                            )

                            if (expenseId != 0) {
                                val updateExpense = Expense(
                                    expenseId = expenseId,
                                    description = descriptionState.text,
                                    amount = amountState.text.toDoubleOrNull() ?: 0.0,
                                    categoryId = categoryId,
                                    date = selectedDate,
                                    paymentMethod = paymentMethodState.text,
                                    bankAccountId = if (paymentMethodState.text == "Bank Account") bankAccountIdState.text.toIntOrNull() else null,
                                    creditCardId = if (paymentMethodState.text == "Credit Card") creditCardIdState.text.toIntOrNull() else null,
                                    cashId = if (paymentMethodState.text == "Cash") cashIdState.text.toIntOrNull() else null,
                                    paymentId = 1
                                )

                                val TAG = "Expense Screen"

                                val amounToAddBack: Double = expense?.amount ?: 0.0
                                val updatedAmount = amountState.text.toDoubleOrNull() ?: 0.0
                                val oldPaymentModeState = expense?.paymentMethod
                                val newPaymentModeState = paymentMethodState.text

                                Log.d(TAG, "$oldPaymentModeState $newPaymentModeState")

                                if (oldPaymentModeState.equals(newPaymentModeState)) {
                                    if (oldPaymentModeState == "Bank Account") {
                                        Log.d(TAG, "Inside Bank account block")
                                        if (expense?.bankAccountId != null) {
                                            expense!!.bankAccountId?.let {
                                                expenseViewModel.addBackAccountBalanceById(
                                                    accountId = it,
                                                    amountToAddBack = amounToAddBack
                                                )
                                            }
                                        }
                                    }
                                } else {
                                    Log.d("Expense", "Inside else block")
                                    if (oldPaymentModeState == "Cash") {
                                        val updateCash = cashLiveData
                                        updateCash?.amount =
                                            updateCash?.amount?.minus(amounToAddBack)!!

                                        accountsViewModel.updateCash(updateCash)
                                    } else if (oldPaymentModeState == "Bank Account") {
                                        if (expense?.bankAccountId != null) {
                                            expense!!.bankAccountId?.let {
                                                expenseViewModel.addBackAccountBalanceById(
                                                    accountId = it,
                                                    amountToAddBack = amounToAddBack
                                                )
                                            }
                                        }
                                    } else if (oldPaymentModeState == "Credit Card") {
                                        if (creditCardIdState.text != null && creditCardIdState.text.isNotEmpty()) {
                                            accountsViewModel.updateCurrentBalanceById(
                                                creditCardIdState.text.toInt(),
                                                amounToAddBack
                                            )
                                        }
                                    }
                                }

                                homeViewModel.updateExpense(expense = updateExpense)
                            } else {
                                val newExpense = Expense(
                                    description = descriptionState.text,
                                    amount = amountState.text.toDoubleOrNull() ?: 0.0,
                                    categoryId = categoryId,
                                    date = selectedDate,
                                    paymentMethod = paymentMethodState.text,
                                    bankAccountId = if (paymentMethodState.text == "Bank Account") bankAccountIdState.text.toIntOrNull() else null,
                                    creditCardId = if (paymentMethodState.text == "Credit Card") creditCardIdState.text.toIntOrNull() else null,
                                    cashId = if (paymentMethodState.text == "Cash") cashIdState.text.toIntOrNull() else null,
                                    paymentId = 1
                                )
                                homeViewModel.insertExpense(newExpense)

                                val amountToDeduct = amountState.text.toDouble()
                                if (paymentMethodState.text == "Cash") {
                                    val updateCash = cashLiveData
                                    updateCash?.amount = updateCash?.amount?.minus(amountToDeduct)!!

                                    accountsViewModel.updateCash(updateCash)
                                } else if (paymentMethodState.text == "Bank Account") {
                                    if ((bankAccountIdState.text != null) && bankAccountIdState.text.isNotEmpty()) {
                                        accountsViewModel.updateAccountBalanceById(
                                            bankAccountIdState.text.toInt(),
                                            amountToDeduct
                                        )
                                    }
                                } else if (paymentMethodState.text == "Credit Card") {
                                    if (creditCardIdState.text != null && creditCardIdState.text.isNotEmpty()) {
                                        accountsViewModel.updateCurrentBalanceById(
                                            creditCardIdState.text.toInt(),
                                            amountToDeduct
                                        )
                                    }
                                }
                            }
                            navHostController.popBackStack()
                        },
                        modifier = Modifier.weight(1f),
                        enabled = categoryId != 0 && amountState.text.isNotBlank()
                    ) {
                        Text(if (expenseId != 0) "Update" else "Save")
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
                    val selectedDateTime =
                        LocalDateTime.ofInstant(currentDateTime.toInstant(), ZoneId.systemDefault())

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