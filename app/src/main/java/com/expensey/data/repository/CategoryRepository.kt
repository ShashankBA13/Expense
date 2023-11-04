package com.expensey.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.LiveData
import com.expensey.data.models.Category
import com.expensey.data.room.dao.CategoryDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class CategoryRepository(private val categoryDao : CategoryDao, private val context: Context) {

	val categoryLiveDataList : LiveData<List<Category>> = categoryDao.fetchAllCategories()

	val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "isCategoriesPopulated")

	val IS_CATEGORIES_POPULATED = booleanPreferencesKey("is_categories_populated")

	val isCategoriesPopulatedFlow: Flow<Boolean> = context.dataStore.data.map { preferences ->
		preferences[IS_CATEGORIES_POPULATED] ?: false
	}

	// Function to populate default categories if not already populated.
	suspend fun populateDefaultCategories() {
		val isCategoriesPopulated = isCategoriesPopulatedFlow.first()
		if (!isCategoriesPopulated) {
			// Populate default categories here.
			val defaultCategories = listOf(
				Category(categoryName = "Food"),
				Category(categoryName = "Rent"),
				Category(categoryName = "Utilities"),
				Category(categoryName = "Transportation"),
				Category(categoryName = "Entertainment"),
				Category(categoryName = "Healthcare"),
				Category(categoryName = "Groceries"),
				Category(categoryName = "Dining Out"),
				Category(categoryName = "Shopping"),
				Category(categoryName = "Travel")
			)

			for (category in defaultCategories) {
				categoryDao.insert(category)
			}
			setIsDataPopulated(true)
		}
	}

	suspend fun insertCategory(category : Category) {
		categoryDao.insert(category)
	}

	suspend fun updateCategory(category : Category) {
		categoryDao.update(category)
	}

	suspend fun deleteCategory(category : Category) {
		categoryDao.delete(category)
	}

	private suspend fun setIsDataPopulated(value: Boolean) {
		context.dataStore.edit { settings ->
			settings[IS_CATEGORIES_POPULATED] = value
		}
	}
}