package com.tuan.tracker_domain.use_case

import com.tuan.core.domain.model.ActivityLevel
import com.tuan.core.domain.model.Gender
import com.tuan.core.domain.model.GoalType
import com.tuan.core.domain.model.UserInfo
import com.tuan.core.domain.preferences.Preferences
import com.tuan.tracker_domain.model.MealType
import com.tuan.tracker_domain.model.TrackedFood
import com.tuan.tracker_domain.util.Constants.CARBS_TO_CALORIES
import com.tuan.tracker_domain.util.Constants.FAT_TO_CALORIES
import com.tuan.tracker_domain.util.Constants.PROTEIN_TO_CALORIES
import kotlin.math.roundToInt

/**
 * - Calculate carbs, protein, fat and calories GOAL, based on the information that the user chose
 * - Calculate CONSUMED carbs, protein, fat and calories PER DAY based on foods that the user have eaten
 * - Calculate CONSUMED carbs, protein, fat and calories FOR EACH MEAL based on foods that the user have eaten
 */
class CalculateMealNutrients(
    private val preferences: Preferences
) {

    /**
     *
     * @param trackedFoods the lists of food from database in a given day
     */
    operator fun invoke(trackedFoods: List<TrackedFood>): Result {

        // calculate consumed nutrients for every mealType
        val mealNutrients: Map<MealType, MealNutrients> = trackedFoods
            .groupBy { it.mealType }
            .mapValues { entry ->
                val mealType: MealType = entry.key
                val foods: List<TrackedFood> = entry.value
                MealNutrients(
                    carb = foods.sumOf { it.carb },
                    protein = foods.sumOf { it.protein },
                    fat = foods.sumOf { it.fat },
                    calories = foods.sumOf { it.calories },
                    mealType = mealType
                )
            }

        // calculate total consumed nutrients
        val totalCarb = mealNutrients.values.sumOf { it.carb }
        val totalProtein = mealNutrients.values.sumOf { it.protein }
        val totalFat = mealNutrients.values.sumOf { it.fat }
        val totalCalories = mealNutrients.values.sumOf { it.calories }

        // calculate nutrients goal
        val userInfo: UserInfo = preferences.loadUserInfo()
        val caloriesGoal = dailyCaloriesRequirement(userInfo = userInfo)
        val carbsGoal = (caloriesGoal * userInfo.carbRatio / CARBS_TO_CALORIES).roundToInt()
        val proteinGoal = (caloriesGoal * userInfo.proteinRatio / PROTEIN_TO_CALORIES).roundToInt()
        val fatGoal = (caloriesGoal * userInfo.fatRatio / FAT_TO_CALORIES).roundToInt()

        return Result(
            carbGoal = carbsGoal,
            proteinGoal = proteinGoal,
            fatGoal = fatGoal,
            caloriesGoal = caloriesGoal,
            totalCarb = totalCarb,
            totalProtein = totalProtein,
            totalFat = totalFat,
            totalCalories = totalCalories,
            mealNutrients = mealNutrients
        )
    }

    /**
     * Calculate bmr index, which how many calories a human burns without doing anything
     *
     * @param userInfo user info
     * @return the minimum amount of calories that a human must use
     */
    private fun bmr(userInfo: UserInfo): Int {
        return when (userInfo.gender) {
            is Gender.Male -> {
                (66.47f + 13.75f * userInfo.weight +
                        5f * userInfo.height - 6.75f * userInfo.age).roundToInt()
            }
            is Gender.Female -> {
                (665.09f + 9.56f * userInfo.weight +
                        1.84f * userInfo.height - 4.67 * userInfo.age).roundToInt()
            }
        }
    }

    /**
     * Calculate the goal calories
     *
     * @param userInfo user info
     * @return the calories goal
     */
    private fun dailyCaloriesRequirement(userInfo: UserInfo): Int {
        val activityFactor = when (userInfo.activityLevel) {
            is ActivityLevel.Low -> 1.2f
            is ActivityLevel.Medium -> 1.3f
            is ActivityLevel.High -> 1.4f
        }

        val caloriesExtra = when (userInfo.goalType) {
            is GoalType.LoseWeight -> -500
            is GoalType.KeepWeight -> 0
            is GoalType.GainWeight -> 500
        }

        return (bmr(userInfo = userInfo) * activityFactor + caloriesExtra).roundToInt()
    }

    /**
     * Defines a meal nutrients
     */
    data class MealNutrients(
        val carb: Int,
        val protein: Int,
        val fat: Int,
        val calories: Int,
        val mealType: MealType
    )

    /**
     * Defines nutrients goal (based on preferences) AND consumed nutrients so far
     *
     * Map:
     * Breakfast -> has its own calculated MealNutrients()
     * Lunch -> has its own calculated MealNutrients()
     * Dinner -> has its own calculated MealNutrients()
     * Snack -> has its own calculated MealNutrients()
     * -> we can calculate the amount of nutrients that user has consumed
     */
    data class Result(
        val carbGoal: Int,
        val proteinGoal: Int,
        val fatGoal: Int,
        val caloriesGoal: Int,
        val totalCarb: Int,
        val totalProtein: Int,
        val totalFat: Int,
        val totalCalories: Int,
        val mealNutrients: Map<MealType, MealNutrients>
    )
}