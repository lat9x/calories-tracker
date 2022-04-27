package com.tuan.onboarding_presentation.nutrient_goal

sealed class NutrientGoalEvent {
    data class OnCarbPercentageEnter(val carbPercentage: String) : NutrientGoalEvent()
    data class OnProteinPercentageEnter(val proteinPercentage: String) : NutrientGoalEvent()
    data class OnFatPercentageEnter(val fatPercentage: String) : NutrientGoalEvent()
    object OnNextClick : NutrientGoalEvent()
}
