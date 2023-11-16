package com.expensey.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
	tableName = "paymentMode"
)
data class PaymentMode(

	@PrimaryKey(autoGenerate = true)
	@ColumnInfo(name = "id")
	val paymentModeId : Int = 0,


)