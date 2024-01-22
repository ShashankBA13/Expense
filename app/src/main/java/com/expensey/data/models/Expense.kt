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
			entity = Category::class,
			parentColumns = ["id"],
			childColumns = ["category_id"],
			onDelete = ForeignKey.SET_NULL
		)
	],
	indices = [
		Index("category_id"),
		Index("payment_id")
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

	@ColumnInfo(name = "payment_id")
	var paymentId : Int

//	/** Based on the payment method this can point to either
//	  * cash, credit card or bank account
//    */
//	@ColumnInfo(name = "payment_mode_id")
//	var paymentModeId : Int,
)
