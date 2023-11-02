package com.expensey.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.Date

@Entity(
	tableName = "credit_card",
	indices = [
		Index(value =  ["card_number"], unique = true)
	]
)
data class CreditCard (

	@PrimaryKey(autoGenerate = true)
	@ColumnInfo(name = "id")
	val creditCardId : Int = 0,

	@ColumnInfo(name = "name")
	val name : String,

	@ColumnInfo(name = "card_holder")
	val cardHolder : String,

	@ColumnInfo(name = "current_balance")
	val currentBalance : Double,

	@ColumnInfo(name = "total_limit")
	val totalLimit : Double,

	@ColumnInfo(name = "card_number")
	val cardNumber : Int,

	@ColumnInfo(name = "bill_generation_date")
	val billGenerationDate : Date,

	@ColumnInfo(name = "bill_payment_date")
	val billPaymentDate : Date,

	@ColumnInfo(name = "notify_user_of")
	val notifyUserOfBillDate : Boolean,

	@ColumnInfo(name = "interest_rate")
	val interestRate : Double,

	@ColumnInfo(name = "cvv")
	val cvv : Int,

	@ColumnInfo(name = "expiration_date")
	val expirationDate : Date
)
