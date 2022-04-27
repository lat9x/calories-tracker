package com.tuan.tracker_presentation.tracker_overview


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tuan.tracker_domain.use_case.CalculateMealNutrients
import com.tuan.tracker_domain.use_case.TrackerUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TrackerOverviewViewModel @Inject constructor(
    private val useCases: TrackerUseCases
) : ViewModel() {

    var state: TrackerOverviewState by mutableStateOf(value = TrackerOverviewState())
        private set

    private var getFoodsForDateJob: Job? = null

    init {
        refreshFoods()
    }

    fun onEvent(event: TrackerOverviewEvent) {
        when (event) {
            is TrackerOverviewEvent.OnDeleteTrackedFoodClick -> {
                viewModelScope.launch {
                    useCases.deleteTrackedFood(event.trackedFood)
                    refreshFoods()
                }
            }
            is TrackerOverviewEvent.OnNextDayClick -> {
                state = state.copy(
                    date = state.date.plusDays(1)
                )
                refreshFoods()
            }
            is TrackerOverviewEvent.OnPreviousDayClick -> {
                state = state.copy(
                    date = state.date.minusDays(1)
                )
                refreshFoods()
            }
            is TrackerOverviewEvent.OnToggleMealClick -> {
                state = state.copy(
                    meals = state.meals.map { meal ->
                        if (meal.name == event.meal.name) {
                            meal.copy(isExpanded = !meal.isExpanded)
                        } else {
                            meal
                        }
                    }
                )
            }
        }
    }

    /**
     * Gets foods from the database, and refresh UI state
     */
    private fun refreshFoods() {
        getFoodsForDateJob?.cancel()

        getFoodsForDateJob = useCases
            .getFoodsForDate(date = state.date)
            .onEach { foods ->
                val nutrientsResult: CalculateMealNutrients.Result =
                    useCases.calculateMealNutrients(trackedFoods = foods)

                state = state.copy(
                    totalCarbs = nutrientsResult.totalCarb,
                    totalProtein = nutrientsResult.totalProtein,
                    totalFat = nutrientsResult.totalFat,
                    totalCalories = nutrientsResult.totalCalories,
                    carbsGoal = nutrientsResult.carbGoal,
                    proteinGoal = nutrientsResult.proteinGoal,
                    fatGoal = nutrientsResult.fatGoal,
                    caloriesGoal = nutrientsResult.caloriesGoal,
                    trackedFoods = foods,
                    meals = state.meals.map { meal ->
                        val nutrientsForMeal: CalculateMealNutrients.MealNutrients =
                            nutrientsResult.mealNutrients[meal.mealType]
                                ?: return@map meal.copy(
                                    carb = 0,
                                    protein = 0,
                                    fat = 0,
                                    calories = 0
                                )
                        meal.copy(
                            carb = nutrientsForMeal.carb,
                            protein = nutrientsForMeal.protein,
                            fat = nutrientsForMeal.fat,
                            calories = nutrientsForMeal.calories
                        )
                    }
                )
            }
            .launchIn(viewModelScope)
    }
}