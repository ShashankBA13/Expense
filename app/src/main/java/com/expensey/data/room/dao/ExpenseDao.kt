package com.expensey.data.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.expensey.data.models.CategoryCount
import com.expensey.data.models.Expense
import com.expensey.data.models.ExpenseSummary
import kotlinx.coroutines.flow.Flow
import java.util.Date

/**
 * [ROOM] DAO for [Expense] related operations
 */
@Dao
abstract class ExpenseDao : BaseDao<Expense> {

	@Query("SELECT expenses.* FROM expenses")
	abstract fun getAllExpenses() : Flow<List<Expense>>

	@Query("SELECT * FROM expenses WHERE payment_method = :paymentMethod")
	abstract fun getExpensesByPaymentMethod(paymentMethod: String): Flow<List<Expense>>

	@Query("SELECT * FROM expenses WHERE date BETWEEN :startDate AND :endDate")
	abstract fun getExpensesByDateRange(startDate: Date, endDate: Date): Flow<List<Expense>>

	@Query("SELECT ROUND(SUM(amount), 3) FROM expenses WHERE category_id = :categoryId")
	abstract fun getTotalExpensesForCategory(categoryId: Int): Flow<Double>

	@Query("SELECT * FROM expenses WHERE id = :expenseId")
	abstract fun getExpenseById(expenseId: Int): Flow<Expense>

	@Query("DELETE FROM expenses WHERE category_id = :categoryId")
	abstract suspend fun deleteExpensesByCategory(categoryId: Int)

	@Query(" SELECT ROUND(SUM(amount), 3) FROM expenses")
	abstract fun totalSumOfExpenses() : Flow<Double>
	
	@Query("""
			SELECT  cat.name AS categoryName,  COUNT(category_id) AS categoryCount, ROUND(SUM(amount), 3) as amountSpent, category_id AS categoryId
			FROM expenses AS exp
			LEFT JOIN
			category AS cat ON cat.id = exp.category_id
			GROUP BY exp.category_id
			ORDER BY SUM(amount) DESC
	""")
	abstract fun mostSpentCategoryListDesc() : LiveData<List<ExpenseSummary>>
}
