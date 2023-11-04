package com.expensey

import android.app.Application
import android.util.Log
import androidx.room.Room
import com.expensey.data.room.database.ExpenseyDatabase

class ExpenseyApplication : Application() {
	lateinit var database: ExpenseyDatabase

	val TAG = "ExpenseyApplication"
	override fun onCreate() {
		super.onCreate()
		Log.d(TAG, "onCreate Method Called")
		database = Room.databaseBuilder(this, ExpenseyDatabase::class.java, "expensey_database").build()
	}

	override fun onTerminate() {
		super.onTerminate()
		Log.d(TAG, "onTerminate Method Called")
		database.close()
	}
}