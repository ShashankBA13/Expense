package com.expensey.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
	tableName = "cash",
	indices = [
		Index("name", unique = true)
	]
)
data class Cash (

	@PrimaryKey(autoGenerate = true)
	@ColumnInfo(name = "id")
	val cashId : Int = 0,

	@ColumnInfo(name = "name")
	val name : String,

	@ColumnInfo(name = "amount")
	var amount : Double = 100.0
)
