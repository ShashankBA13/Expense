package com.expensey.ui.screens.category

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.expensey.ExpenseyApplication
import com.expensey.data.models.Category
import com.expensey.data.repository.CategoryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class CategoryViewModel(application: Application) : AndroidViewModel(application){

	val categoryLiveDataList : LiveData<List<Category>>
	private val categoryRepository : CategoryRepository

	val TAG = "CategoryViewModel"

	init {
		val expenseyApplication = application as ExpenseyApplication
		val categoryDao = expenseyApplication.database.categoryDao()
		categoryRepository = CategoryRepository(categoryDao, expenseyApplication.baseContext)
		populateDefaultCategories()
		categoryLiveDataList = categoryRepository.categoryLiveDataList
	}

	fun populateDefaultCategories() {
		viewModelScope.launch {
			categoryRepository.populateDefaultCategories()
		}
	}

	fun addCategory(category : Category) {
		viewModelScope.launch(Dispatchers.IO) {
			categoryRepository.insertCategory(category)
		}
	}

	fun updateCategory(category : Category) {
		viewModelScope.launch {
			categoryRepository.updateCategory(category)
		}
	}

	fun deleteCategory(category : Category) {
		viewModelScope.launch {
			categoryRepository.deleteCategory(category)
		}
	}

	fun getCategoryById(categoryId : Int) : Flow<Category> {
		return categoryRepository.getCategoryById(categoryId)
	}

}