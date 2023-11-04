package com.expensey.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
	tableName = "category",
	indices = [
		Index(value =  ["name"], unique = true)
	]
)
data class Category (

	@PrimaryKey(autoGenerate = true)
	@ColumnInfo(name = "id")
	val categoryId : Int = 0,

	@ColumnInfo(name = "name")
	var categoryName : String
)