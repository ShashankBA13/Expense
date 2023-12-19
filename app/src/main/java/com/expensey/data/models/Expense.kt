package com.expensey.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.Date

@Entity(
	tableName = "expenses",
	foreignKeys = [
		ForeignKey(
			entity = BankAccount::class,
			parentColumns = ["id"],
			childColumns = ["bank_account_id"],
			onDelete = ForeignKey.SET_NULL
		),
		ForeignKey(
			entity = CreditCard::class,
			parentColumns = ["id"],
			childColumns = ["credit_card_id"],
			onDelete = ForeignKey.SET_NULL
		),
		ForeignKey(
			entity = Cash::class,
			parentColumns = ["id"],
			childColumns = ["cash_id"],
			onDelete = ForeignKey.SET_NULL
		),
		ForeignKey(
			entity = Category::class,
			parentColumns = ["id"],
			childColumns = ["category_id"],
			onDelete = ForeignKey.SET_NULL
		)
	],
	indices = [
		Index("bank_account_id"),
		Index("credit_card_id"),
		Index("cash_id"),
		Index("category_id")
	]
)
data class Expense (

	@PrimaryKey(autoGenerate = true)
	@ColumnInfo(name = "id")
	val expenseId : Int = 0,

	@ColumnInfo(name = "description")
	val description: String,

	@ColumnInfo(name = "amount")
	val amount: Double,

	@ColumnInfo(name = "category_id")
	val categoryId : Int?,

	@ColumnInfo(name = "date")
	val date: Date,

	@ColumnInfo(name = "payment_method")
	val paymentMethod: String,

	@ColumnInfo(name = "bank_account_id")
	var bankAccountId: Int?,

	@ColumnInfo(name = "credit_card_id")
	var creditCardId: Int?,

	@ColumnInfo(name = "cash_id")
	var cashId: Int?

//	/** Based on the payment method this can point to either
//	  * cash, credit card or bank account
//    */
//	@ColumnInfo(name = "payment_mode_id")
//	var paymentModeId : Int,
)
