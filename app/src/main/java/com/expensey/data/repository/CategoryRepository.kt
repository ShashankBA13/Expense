package com.expensey.data.repository

import androidx.lifecycle.LiveData
import com.expensey.data.models.Category
import com.expensey.data.room.dao.CategoryDao

class CategoryRepository(private val categoryDao : CategoryDao) {

	val categoryLiveDataList : LiveData<List<Category>> = categoryDao.fetchAllCategories()

	private var isDataPopulated = false

	// Function to populate default categories if not already populated.
	suspend fun populateDefaultCategories() {
		if (!isDataPopulated) {
			// Populate default categories here.
			val defaultCategories = listOf(
				Category(categoryName = "Category 1"),
				Category(categoryName = "Category 2"),
				// Add more default categories as needed.
			)
			for (category in defaultCategories) {
				categoryDao.insert(category)
			}
			isDataPopulated = true
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
}