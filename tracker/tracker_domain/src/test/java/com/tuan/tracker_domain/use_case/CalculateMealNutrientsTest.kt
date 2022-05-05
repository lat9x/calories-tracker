package com.tuan.tracker_domain.use_case

import com.google.common.truth.Truth.assertThat
import com.tuan.core.domain.model.ActivityLevel
import com.tuan.core.domain.model.Gender
import com.tuan.core.domain.model.GoalType
import com.tuan.core.domain.model.UserInfo
import com.tuan.core.domain.preferences.Preferences
import com.tuan.tracker_domain.model.MealType
import com.tuan.tracker_domain.model.TrackedFood
import io.mockk.every
import io.mockk.mockk
import org.junit.Before
import org.junit.Test
import java.time.LocalDate
import kotlin.random.Random

class CalculateMealNutrientsTest {

    private lateinit var calculateMealNutrients: CalculateMealNutrients

    @Before
    fun setup() {
        val preferences = mockk<Preferences>(relaxed = true)
        every { preferences.loadUserInfo() } returns UserInfo(
            gender = Gender.Male,
            age = 20,
            weight = 80f,
            height = 150,
            activityLevel = ActivityLevel.Medium,
            goalType = GoalType.KeepWeight,
            carbRatio = 0.4f,
            proteinRatio = 0.3f,
            fatRatio = 0.3f
        )
        calculateMealNutrients = CalculateMealNutrients(preferences = preferences)
    }

    @Test
    fun `Calories for breakfast properly calculated`() {
        val trackedFoods = (1..30).map {
            TrackedFood(
                name = "name",
                carb = Random.nextInt(until = 100),
                protein = Random.nextInt(until = 100),
                fat = Random.nextInt(until = 100),
                mealType = MealType.fromString(
                    name = listOf("breakfast", "lunch", "dinner", "snack").random()
                ),
                imageUrl = null,
                amount = 100,
                date = LocalDate.now(),
                calories = Random.nextInt(until = 2000)
            )
        }

        val result = calculateMealNutrients(trackedFoods = trackedFoods)

        val breakfastCalories = result.mealNutrients.values
            .filter { it.mealType is MealType.Breakfast }
            .sumOf { it.calories }
        val expectedCalories = trackedFoods
            .filter { it.mealType is MealType.Breakfast }
            .sumOf { it.calories }

        assertThat(breakfastCalories).isEqualTo(expectedCalories)
    }

    @Test
    fun `Carbs for dinner properly calculated`() {
        val trackedFoods = (1..30).map {
            TrackedFood(
                name = "name",
                carb = Random.nextInt(until = 100),
                protein = Random.nextInt(until = 100),
                fat = Random.nextInt(until = 100),
                mealType = MealType.fromString(
                    name = listOf("breakfast", "lunch", "dinner", "snack").random()
                ),
                imageUrl = null,
                amount = 100,
                date = LocalDate.now(),
                calories = Random.nextInt(until = 2000)
            )
        }

        val result = calculateMealNutrients(trackedFoods = trackedFoods)

        val dinnerCarbs = result.mealNutrients.values
            .filter { it.mealType is MealType.Dinner }
            .sumOf { it.carb }
        val expectedCarbs = trackedFoods
            .filter { it.mealType is MealType.Dinner }
            .sumOf { it.carb }

        assertThat(dinnerCarbs).isEqualTo(expectedCarbs)
    }
}