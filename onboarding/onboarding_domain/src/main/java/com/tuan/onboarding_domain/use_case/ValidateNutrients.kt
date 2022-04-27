package com.tuan.onboarding_domain.use_case

import com.tuan.core.util.UiText
import com.tuan.core.R
import com.tuan.onboarding_domain.util.Constants.ONE_HUNDRED_PERCENT
import com.tuan.onboarding_domain.util.Constants.TO_DECIMAL

class ValidateNutrients {

    operator fun invoke(
        carbPercentageText: String,
        proteinPercentageText: String,
        fatPercentageText: String,
    ): Result {
        val carbPercentage: Int? = carbPercentageText.toIntOrNull()
        val proteinPercentage: Int? = proteinPercentageText.toIntOrNull()
        val fatPercentage: Int? = fatPercentageText.toIntOrNull()

        if (carbPercentage == null || proteinPercentage == null || fatPercentage == null) {
            return Result.Error(
                errorMessage = UiText.StringResource(resId = R.string.error_invalid_values)
            )
        }
        if (carbPercentage + proteinPercentage + fatPercentage != ONE_HUNDRED_PERCENT) {
            return Result.Error(
                errorMessage = UiText.StringResource(resId = R.string.error_not_100_percent)
            )
        }
        return Result.Success(
            carbRatio = carbPercentage / TO_DECIMAL,
            proteinRatio = proteinPercentage / TO_DECIMAL,
            fatRatio = fatPercentage / TO_DECIMAL
        )
    }

    sealed class Result {
        data class Success(
            val carbRatio: Float,
            val proteinRatio: Float,
            val fatRatio: Float
        ) : Result()
        data class Error(val errorMessage: UiText) : Result()
    }
}