package com.expensey.ui.screens.insights

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.expensey.ExpenseyApplication
import com.expensey.data.models.CategoryCount
import com.expensey.data.models.ExpenseSummary
import com.expensey.data.repository.ExpenseRepository
import kotlinx.coroutines.launch

class InsightsViewModel(application : Application) : AndroidViewModel(application)  {

    private val expenseRepository : ExpenseRepository
    lateinit var mostSpentCategoryCountList : List<ExpenseSummary>

    init {
        val expenseyApplication = application as ExpenseyApplication
        val expenseDao = expenseyApplication.database.expenseDao()
        expenseRepository = ExpenseRepository(expenseDao, expenseyApplication.baseContext)

       viewModelScope.launch {
           mostSpentCategoryCountList = expenseRepository.getMostSpentCategoryCout()
       }
    }
}