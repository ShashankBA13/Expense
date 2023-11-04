package com.expensey.data.repository

import androidx.lifecycle.LiveData
import com.expensey.data.models.Category
import com.expensey.data.room.dao.CategoryDao

class CategoryRepository(private val categoryDao : CategoryDao) {

	val categoryLiveDataList : LiveData<List<Category>> = categoryDao.fetchAllCategories()

	suspend fun insertCategory(category : Category) {
		categoryDao.insert(category)
	}
}