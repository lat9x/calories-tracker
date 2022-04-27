package com.tuan.tracker_presentation.tracker_overview

import androidx.annotation.DrawableRes
import com.tuan.core.util.UiText
import com.tuan.tracker_domain.model.MealType
import com.tuan.core.R

/**
 * Represents a single meal object in UI
 */
data class Meal(
    val name: UiText,
    @DrawableRes val drawableRes: Int,
    val mealType: MealType,
    val carb: Int = 0,
    val protein: Int = 0,
    val fat: Int = 0,
    val calories: Int = 0,
    val isExpanded: Boolean = false
)

val defaultMeals: List<Meal> = listOf(
    Meal(
        name = UiText.StringResource(resId = R.string.breakfast),
        drawableRes = R.drawable.ic_breakfast,
        mealType = MealType.Breakfast
    ),
    Meal(
        name = UiText.StringResource(resId = R.string.lunch),
        drawableRes = R.drawable.ic_lunch,
        mealType = MealType.Lunch
    ),
    Meal(
        name = UiText.StringResource(resId = R.string.dinner),
        drawableRes = R.drawable.ic_dinner,
        mealType = MealType.Dinner
    ),
    Meal(
        name = UiText.StringResource(resId = R.string.snacks),
        drawableRes = R.drawable.ic_snack,
        mealType = MealType.Snack
    ),
)