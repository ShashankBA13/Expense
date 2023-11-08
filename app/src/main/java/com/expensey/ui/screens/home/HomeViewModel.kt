package com.expensey.ui.screens.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.expensey.ExpenseyApplication
import com.expensey.data.models.Expense
import com.expensey.data.repository.ExpenseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.util.Date

class HomeViewModel(application : Application) : AndroidViewModel(application) {

	private val expenseRepository : ExpenseRepository

	private val expenseFlowList : Flow<List<Expense>>

	init {
		val expenseyApplication = application as ExpenseyApplication
		val expenseDao = expenseyApplication.database.expenseDao()
		expenseRepository = ExpenseRepository(expenseDao, expenseyApplication.baseContext)

		expenseFlowList = expenseRepository.expenseFlowList
	}

	fun insertExpense(expense : Expense) {
		viewModelScope.launch {
			expenseRepository.insertExpense(expense)
		}
	}

	fun updateExpense(expense : Expense) {
		viewModelScope.launch {
			expenseRepository.updateExpense(expense)
		}
	}

	fun getExpenseById(expenseId: Int): Flow<Expense> {
		return expenseRepository.getExpenseById(expenseId)
	}

	suspend fun deleteExpense(expense : Expense) {
		expenseRepository.deleteExpense(expense)
	}

	fun getExpensesByPaymentMethod(paymentMethod: String): Flow<List<Expense>> {
		return expenseRepository.getExpensesByPaymentMethod(paymentMethod)
	}

	fun getExpensesByDateRange(startDate: Date, endDate: Date): Flow<List<Expense>> {
		return expenseRepository.getExpensesByDateRange(startDate, endDate)
	}

	fun getTotalExpensesForCategory(categoryId: Int): Flow<Double> {
		return expenseRepository.getTotalExpensesForCategory(categoryId)
	}
}