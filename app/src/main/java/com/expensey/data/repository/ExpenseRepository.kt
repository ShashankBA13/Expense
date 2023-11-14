package com.expensey.data.repository

import android.content.Context
import com.expensey.data.models.Expense
import com.expensey.data.room.dao.ExpenseDao
import kotlinx.coroutines.flow.Flow
import java.util.Date

class ExpenseRepository(private val expenseDao : ExpenseDao, private val context : Context) {

	val expenseFlowList : Flow<List<Expense>> = expenseDao.getAllExpenses()

	suspend fun insertExpense(expense : Expense) {
		expenseDao.insert(expense)
	}

	suspend fun updateExpense(expense : Expense) {
		expenseDao.update(expense)
	}

	fun getExpenseById(expenseId: Int): Flow<Expense> {
		return expenseDao.getExpenseById(expenseId)
	}

	suspend fun deleteExpense(expense : Expense) {
		expenseDao.delete(expense)
	}

	fun getExpensesByPaymentMethod(paymentMethod: String): Flow<List<Expense>> {
		return expenseDao.getExpensesByPaymentMethod(paymentMethod)
	}

	fun getExpensesByDateRange(startDate: Date, endDate: Date): Flow<List<Expense>> {
		return expenseDao.getExpensesByDateRange(startDate, endDate)
	}

	fun getTotalExpensesForCategory(categoryId: Int): Flow<Double> {
		return expenseDao.getTotalExpensesForCategory(categoryId)
	}

	fun getTotalSumOfExpenses() : Flow<Double> {
		return expenseDao.totalSumOfExpenses()
	}
}