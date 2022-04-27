package com.tuan.tracker_domain.use_case

import com.tuan.tracker_domain.model.MealType
import com.tuan.tracker_domain.model.TrackableFood
import com.tuan.tracker_domain.model.TrackedFood
import com.tuan.tracker_domain.repository.TrackerRepository
import java.time.LocalDate
import kotlin.math.roundToInt

class TrackFood(
    private val repository: TrackerRepository
) {

    /**
     * Insert chosen food to the database in order to track it
     *
     * @param food the food's basic information from OpenFoodApi
     * @param amount food amount (in form of grams)
     * @param mealType can be either breakfast
     * @param date contains dayOfMonth, month, and year
     */
    suspend operator fun invoke(
        food: TrackableFood,
        amount: Int,
        mealType: MealType,
        date: LocalDate
    ) {
        repository.insertTrackedFood(
            food = TrackedFood(
                name = food.name,
                calories = ((food.caloriesPer100g / TO_PER_GRAM) * amount).roundToInt(),
                carb = ((food.carbPer100g / TO_PER_GRAM) * amount).roundToInt(),
                protein = ((food.proteinPer100g / TO_PER_GRAM) * amount).roundToInt(),
                fat = ((food.fatPer100g / TO_PER_GRAM) * amount).roundToInt(),
                imageUrl = food.imageUrl,
                mealType = mealType,
                amount = amount,
                date = date,
            )
        )
    }

    companion object {
        private const val TO_PER_GRAM = 100f
    }
}