package com.expensey.data.models

import java.math.BigDecimal

data class ExpenseSummary(
    val categoryName: String,
    val categoryCount: Int,
    val amountSpent: Double,
    val categoryId: Int
)

