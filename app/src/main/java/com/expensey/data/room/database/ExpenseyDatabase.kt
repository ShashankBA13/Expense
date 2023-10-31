package com.expensey.data.room.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
	entities = [

	],
	version = 1,
	exportSchema = false
)
@TypeConverters()
abstract class ExpenseyDatabase : RoomDatabase() {
}