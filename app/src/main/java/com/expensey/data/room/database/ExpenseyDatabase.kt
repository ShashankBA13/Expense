package com.expensey.data.room.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.expensey.data.models.BankAccount
import com.expensey.data.models.Cash
import com.expensey.data.models.Category
import com.expensey.data.models.CreditCard
import com.expensey.data.models.Expense
import com.expensey.data.room.dao.BankAccountDao
import com.expensey.data.room.dao.CashDao
import com.expensey.data.room.dao.CategoryDao
import com.expensey.data.room.dao.CreditCardDao
import com.expensey.data.room.dao.ExpenseDao
import com.expensey.data.typeconverters.DateConverter

@Database(
	entities = [
		BankAccount :: class,
		Cash        :: class,
		Category    :: class,
		CreditCard  :: class,
		Expense     :: class
	],
	version = 3,
	exportSchema = false
)
@TypeConverters(DateConverter::class)
abstract class ExpenseyDatabase : RoomDatabase() {

	abstract fun bankAccountDao() : BankAccountDao
	abstract fun cashDao()        : CashDao
	abstract fun categoryDao()    : CategoryDao
	abstract fun creditCardDao()  : CreditCardDao
	abstract fun expenseDao()     : ExpenseDao
}