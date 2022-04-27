package com.tuan.onboarding_domain.use_case

data class OnboardingUseCases(
    val validateAge: ValidateAge,
    val validateHeight: ValidateHeight,
    val validateWeight: ValidateWeight,
    val validateNutrients: ValidateNutrients
)
