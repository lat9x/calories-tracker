package com.tuan.tracker_domain.model

import java.time.LocalDate

data class TrackedFood(
    val name: String,
    val carb: Int,
    val protein: Int,
    val fat: Int,
    val imageUrl: String?,
    val mealType: MealType,
    val amount: Int,
    val date: LocalDate,
    val calories: Int,
    val id: Int? = null
)
