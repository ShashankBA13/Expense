package com.expensey

import android.app.Application
import androidx.room.Room
import com.expensey.data.room.database.ExpenseyDatabase

class ExpenseyApplication : Application() {
	private lateinit var database: ExpenseyDatabase
	override fun onCreate() {
		super.onCreate()

		database = Room.databaseBuilder(this, ExpenseyDatabase::class.java, "expensey_database").build()
	}

	override fun onTerminate() {
		super.onTerminate()
		database.close()
	}
}