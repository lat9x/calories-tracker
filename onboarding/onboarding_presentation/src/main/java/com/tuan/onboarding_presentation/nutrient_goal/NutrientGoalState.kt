package com.tuan.onboarding_presentation.nutrient_goal

import com.tuan.onboarding_presentation.util.Constants.DEFAULT_CARB_PERCENTAGE
import com.tuan.onboarding_presentation.util.Constants.DEFAULT_FAT_PERCENTAGE
import com.tuan.onboarding_presentation.util.Constants.DEFAULT_PROTEIN_PERCENTAGE

data class NutrientGoalState(
    val carbPercentage: String = DEFAULT_CARB_PERCENTAGE,
    val proteinPercentage: String = DEFAULT_PROTEIN_PERCENTAGE,
    val fatPercentage: String = DEFAULT_FAT_PERCENTAGE,
)
