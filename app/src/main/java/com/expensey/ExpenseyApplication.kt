package com.expensey

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import com.expensey.data.room.database.ExpenseyDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ExpenseyApplication : Application() {
	lateinit var database : ExpenseyDatabase

	companion object {
		val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
	}

	val TAG = "ExpenseyApplication"
	override fun onCreate() {
		super.onCreate()
		Log.d(TAG, "onCreate Method Called")
		database =
			Room.databaseBuilder(this, ExpenseyDatabase::class.java, "expensey_database")
				.build()

		val applicationScope = CoroutineScope(Dispatchers.Default)
		applicationScope.launch {
			// Perform any necessary initialization here.
		}
	}

	override fun onTerminate() {
		super.onTerminate()
		Log.d(TAG, "onTerminate Method Called")
		database.close()
	}

}