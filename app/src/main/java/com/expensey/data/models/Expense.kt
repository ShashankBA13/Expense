package com.expensey.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.Date

@Entity(
	tableName = "expenses",
	foreignKeys = [
		ForeignKey(
			entity = BankAccount::class,
			parentColumns = ["id"],
			childColumns = ["bank_account_id"],
			onDelete = ForeignKey.CASCADE
		),
		ForeignKey(
			entity = CreditCard::class,
			parentColumns = ["id"],
			childColumns = ["credit_card_id"],
			onDelete = ForeignKey.CASCADE
		),
		ForeignKey(
			entity = Cash::class,
			parentColumns = ["id"],
			childColumns = ["cash_id"],
			onDelete = ForeignKey.CASCADE
		),
		ForeignKey(
			entity = Category::class,
			parentColumns = ["id"],
			childColumns = ["category_id"],
			onDelete = ForeignKey.CASCADE
		)
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
	val categoryId : Int?, // Foreign key to Category entity

	@ColumnInfo(name = "date")
	val date: Date,

	@ColumnInfo(name = "payment_method")
	val paymentMethod: String,  // This field acts as a discriminator

	@ColumnInfo(name = "bank_account_id")
	val bankAccountId: Int?,  // Foreign key to BankAccount entity

	@ColumnInfo(name = "credit_card_id")
	val creditCardId: Int?,  // Foreign key to CreditCard entity

	@ColumnInfo(name = "cash_id")
	val cashId: Int?  // Foreign key to CreditCard entity
)