package com.expensey.ui.screens.expense

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Build
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
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.expensey.data.models.BankAccount
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

    var oldPaymentMethodState by remember { mutableStateOf(TextFieldValue("")) }
    var oldPaymentIdState by remember { mutableStateOf(TextFieldValue("")) }
    var updatedPaymentIdState by remember { mutableStateOf(TextFieldValue("")) }


    var isCategoryMenuExpanded by remember { mutableStateOf(false) }
    val categoryListState by categoryViewModel.categoryLiveDataList.observeAsState()
    val categories = categoryListState ?: emptyList()
    var selectedCategory by remember { mutableStateOf<Category?>(null) }


    var isPaymentModeExpanded by remember { mutableStateOf(false) }
    val bankAccountsList = bankAccountLiveDataList ?: emptyList()
    val creditCardsList = creditCardLiveDataList ?: emptyList()
    var newPaymentMethodState by remember { mutableStateOf(TextFieldValue("")) }

    var selectedPaymentModeDisplay by remember { mutableStateOf("") }

    if (expense != null) {
        dateState = TextFieldValue(
            DateTimeFormatter.ofPattern("dd MMM yyyy | HH:mm").format(
                expense!!.date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()
            )
        )
        amountState = TextFieldValue(expense!!.amount.toString())
        descriptionState = TextFieldValue(expense!!.description)
        oldPaymentMethodState = TextFieldValue(expense!!.paymentMethod)
        oldPaymentIdState = TextFieldValue(expense!!.paymentId.toString())
        updatedPaymentIdState = TextFieldValue(expense!!.paymentId.toString())

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

        when (oldPaymentMethodState.text) {
            "Bank Account" -> {
                val bankAccountLiveData = accountsViewModel.getBankAccountById(expense!!.paymentId)
                bankAccountLiveData.observeAsState().value?.let { bankAccount = it }
                selectedPaymentModeDisplay = bankAccount?.accountName.toString()
            }

            "Credit Card" -> {
                val creditCardLiveData = accountsViewModel.fetchCreditCardById(expense!!.paymentId)
                creditCardLiveData.observeAsState().value?.let { creditCard = it }
                selectedPaymentModeDisplay = creditCard?.name.toString()
            }

            "Cash" -> {
                selectedPaymentModeDisplay = (selectedPaymentModeDisplay.takeIf { it.isNotBlank() }
                    ?: cashLiveData?.name).toString()
            }
        }
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        Column {
            Row(modifier = Modifier.fillMaxWidth()) {
                Icon(imageVector = Icons.Outlined.ArrowBackIosNew,
                    contentDescription = "Back",
                    modifier = Modifier.clickable {
                        navHostController.popBackStack()
                    } then Modifier.padding(start = 20.dp, top = 20.dp, end = 10.dp))

                Text(
                    text = "Expense",
                    modifier = Modifier
                        .padding(20.dp)
                        .weight(1f),
                    style = Typography.headlineLarge
                )

                if (expenseId != 0) {
                    Icon(imageVector = Icons.Outlined.Delete,
                        contentDescription = "Delete Bank Account",
                        modifier = Modifier.clickable {
                            when (oldPaymentMethodState.text) {
                                "Cash" -> {
                                    val updateCash = cashLiveData
                                    updateCash?.amount =
                                        expense?.let { updateCash?.amount?.plus(it.amount) }!!
                                    if (updateCash != null) {
                                        accountsViewModel.updateCash(updateCash)
                                    }
                                }

                                "Bank Account" -> {
                                    expense?.let {
                                        expenseViewModel.addBackAccountBalanceById(
                                            oldPaymentIdState.text.toInt(), it.amount
                                        )
                                    }
                                }

                                "Credit Card" -> {
                                    expense?.let {
                                        accountsViewModel.addBackDedcutedAmountInCreditCardById(
                                            oldPaymentIdState.text.toInt(), it.amount
                                        )
                                    }
                                }
                            }
                            expense?.let { homeViewModel.deleteExpense(it) }
                            Toast.makeText(context, "Expense Deleted", Toast.LENGTH_SHORT).show()
                            navHostController.popBackStack()
                        } then Modifier.padding(start = 20.dp, top = 20.dp, end = 20.dp),
                        tint = Color.Red)
                }
            }
            Divider(
                color = Color.Gray,
                thickness = 1.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, bottom = 10.dp, end = 20.dp)
            )

            Column(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(value = dateState,
                    onValueChange = {
                        dateState = it
                    },
                    label = { Text("Date") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 20.dp, bottom = 10.dp),
                    trailingIcon = {
                        IconButton(onClick = {
                            showDateTimePicker(context) { selectedDateTime ->
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
                    })

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
                            Icons.Outlined.AttachMoney, contentDescription = "Amount"
                        )
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number
                    )
                )

                OutlinedTextField(value = descriptionState,
                    onValueChange = {
                        descriptionState = it
                    },
                    label = { Text("Description") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 20.dp, bottom = 10.dp),
                    trailingIcon = {
                        Icon(Icons.Outlined.Description, contentDescription = "Description Icon")
                    })

                ExposedDropdownMenuBox(
                    expanded = isCategoryMenuExpanded,
                    onExpandedChange = {
                        isCategoryMenuExpanded = !isCategoryMenuExpanded
                    },
                    modifier = Modifier
                        .padding(start = 20.dp, end = 20.dp, bottom = 10.dp)
                        .fillMaxWidth()
                ) {
                    OutlinedTextField(value = selectedCategory?.categoryName ?: "",
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
                        colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors())

                    ExposedDropdownMenu(
                        expanded = isCategoryMenuExpanded,
                        onDismissRequest = {
                            isCategoryMenuExpanded = false
                        },
                        modifier = Modifier.fillMaxWidth(),

                        ) {
                        categories.forEach { category ->
                            DropdownMenuItem(
                                onClick = {
                                    categoryId = category.categoryId
                                    selectedCategory = category
                                    isCategoryMenuExpanded = false
                                },
                                text = { Text(category.categoryName) },
                                modifier = Modifier.fillMaxWidth()
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
                    OutlinedTextField(value = selectedPaymentModeDisplay,
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
                        colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors())

                    ExposedDropdownMenu(
                        expanded = isPaymentModeExpanded, onDismissRequest = {
                            isPaymentModeExpanded = false
                        }, modifier = Modifier.fillMaxWidth()
                    ) {

                        if (bankAccountsList.isNotEmpty()) {
                            DropdownMenuItem(onClick = {}, text = {
                                Text("Bank Account", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                            })

                            Divider(modifier = Modifier.padding(start = 10.dp, end = 10.dp))
                        }

                        bankAccountsList.forEach { bankAccount ->
                            DropdownMenuItem(onClick = {
                                selectedPaymentModeDisplay = bankAccount.accountName
                                isPaymentModeExpanded = false
                                newPaymentMethodState = TextFieldValue("Bank Account")
                                updatedPaymentIdState =
                                    TextFieldValue(bankAccount.accountId.toString())
                            }, modifier = Modifier.fillMaxWidth(), text = {
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
                            })
                        }

                        if (creditCardsList.isNotEmpty()) {
                            DropdownMenuItem(onClick = {}, text = {
                                Text("Credit Card", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                            })

                            Divider(modifier = Modifier.padding(start = 10.dp, end = 10.dp))
                        }

                        creditCardsList.forEach { creditCard ->
                            DropdownMenuItem(onClick = {
                                selectedPaymentModeDisplay = creditCard.name
                                isPaymentModeExpanded = false
                                newPaymentMethodState = TextFieldValue("Credit Card")
                                updatedPaymentIdState =
                                    TextFieldValue(creditCard.creditCardId.toString())
                            }, modifier = Modifier.fillMaxWidth(), text = {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = creditCard.name, fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "₹ ${creditCard.currentBalance}",
                                        fontWeight = FontWeight.Bold,
                                        color = Color.Gray
                                    )
                                }
                            })
                        }

                        DropdownMenuItem(onClick = {}, text = {
                            Text("Cash", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        })
                        Divider(modifier = Modifier.padding(start = 10.dp, end = 10.dp))

                        DropdownMenuItem(onClick = {
                            selectedPaymentModeDisplay = cashLiveData?.name.toString()
                            isPaymentModeExpanded = false
                            newPaymentMethodState = TextFieldValue("Cash")
                            updatedPaymentIdState = TextFieldValue(cashLiveData?.cashId.toString())
                        }, modifier = Modifier.fillMaxWidth(), text = {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "${cashLiveData?.name}", fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "₹ ${cashLiveData?.amount}",
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Gray
                                )
                            }
                        })
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
                        }, modifier = Modifier.weight(1f)
                    ) {
                        Text("Cancel")
                    }

                    OutlinedButton(
                        onClick = {

                            val selectedDateTime = LocalDateTime.parse(
                                dateState.text, DateTimeFormatter.ofPattern("dd MMM yyyy | HH:mm")
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
                                    paymentMethod = newPaymentMethodState.text,
                                    paymentId = updatedPaymentIdState.text.toInt()
                                )

                                val amountToAddBack: Double = expense?.amount ?: 0.0
                                val amountToDeduct = amountState.text.toDoubleOrNull() ?: 0.0
                                val newPaymentModeState = newPaymentMethodState.text

                                when (oldPaymentMethodState.text) {
                                    "Cash" -> {
                                        val updateCash = cashLiveData
                                        updateCash?.amount =
                                            updateCash?.amount?.plus(amountToAddBack)!!
                                        accountsViewModel.updateCash(updateCash)
                                    }

                                    "Bank Account" -> {
                                        expenseViewModel.addBackAccountBalanceById(
                                            oldPaymentIdState.text.toInt(), amountToAddBack
                                        )
                                    }

                                    "Credit Card" -> {
                                        accountsViewModel.addBackDedcutedAmountInCreditCardById(
                                            oldPaymentIdState.text.toInt(), amountToAddBack
                                        )
                                    }
                                }

                                when (newPaymentModeState) {
                                    "Cash" -> {
                                        val updateCash = cashLiveData
                                        updateCash?.amount =
                                            updateCash?.amount?.minus(amountToDeduct)!!
                                        accountsViewModel.updateCash(updateCash)
                                    }

                                    "Bank Account" -> {
                                        accountsViewModel.updateAccountBalanceById(
                                            updatedPaymentIdState.text.toInt(), amountToDeduct
                                        )
                                    }

                                    "Credit Card" -> {
                                        accountsViewModel.updateCurrentBalanceById(
                                            updatedPaymentIdState.text.toInt(), amountToDeduct
                                        )
                                    }
                                }

                                homeViewModel.updateExpense(expense = updateExpense)
                            } else {
                                val newExpense = Expense(
                                    description = descriptionState.text,
                                    amount = amountState.text.toDoubleOrNull() ?: 0.0,
                                    categoryId = categoryId,
                                    date = selectedDate,
                                    paymentMethod = newPaymentMethodState.text,
                                    paymentId = updatedPaymentIdState.text.toInt()
                                )
                                homeViewModel.insertExpense(newExpense)

                                val amountToDeduct = amountState.text.toDouble()
                                when (newPaymentMethodState.text) {
                                    "Cash" -> {
                                        val updateCash = cashLiveData
                                        updateCash?.amount =
                                            updateCash?.amount?.minus(amountToDeduct)!!
                                        accountsViewModel.updateCash(updateCash)
                                    }

                                    "Bank Account" -> {
                                        accountsViewModel.updateAccountBalanceById(
                                            updatedPaymentIdState.text.toInt(), amountToDeduct
                                        )
                                    }

                                    "Credit Card" -> {
                                        accountsViewModel.updateCurrentBalanceById(
                                            updatedPaymentIdState.text.toInt(), amountToDeduct
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
        context, { _, selectedYear, selectedMonth, selectedDay ->
            currentDateTime.set(Calendar.YEAR, selectedYear)
            currentDateTime.set(Calendar.MONTH, selectedMonth)
            currentDateTime.set(Calendar.DAY_OF_MONTH, selectedDay)
            val hour: Int = currentDateTime.get(Calendar.HOUR_OF_DAY)
            val minute: Int = currentDateTime.get(Calendar.MINUTE)

            // Time Picker Dialog
            val timePickerDialog = TimePickerDialog(
                context, { _, selectedHour, selectedMinute ->
                    currentDateTime.set(Calendar.HOUR_OF_DAY, selectedHour)
                    currentDateTime.set(Calendar.MINUTE, selectedMinute)

                    // Format the selected date and time
                    val selectedDateTime =
                        LocalDateTime.ofInstant(currentDateTime.toInstant(), ZoneId.systemDefault())

                    // Update the dateState value
                    onDateSelected(selectedDateTime)
                }, hour, minute, false
            )
            timePickerDialog.show()
        }, year, month, day
    )
    datePickerDialog.show()
}