package com.expensey.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
	tableName = "bank_account",
	indices = [
		Index(value =  ["account_number"], unique = true)
	]
)
data class BankAccount (

	@PrimaryKey(autoGenerate = true)
	@ColumnInfo("id")
	val accountId : Int =0,

	@ColumnInfo(name = "name")
	val accountName : String,

	@ColumnInfo(name = "current_balance")
	val currentBalance : Double,

	@ColumnInfo(name = "account_number")
	val accountNumber : String,

	@ColumnInfo(name = "account_holder")
	val accountHolderName : String
)