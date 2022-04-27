package com.tuan.onboarding_domain.use_case

import com.tuan.core.R
import com.tuan.core.util.UiText
import com.tuan.onboarding_domain.util.Constants
import com.tuan.onboarding_domain.util.Constants.SECOND_CHARACTER
import com.tuan.onboarding_domain.util.Constants.WEIGHT_MAX_LENGTH

class ValidateWeight {

    /**
     * Validate weight text field
     *
     * @param weight weight in form of string
     */
    operator fun invoke(weight: String): Result {
        val weightInNumber: Float = weight.toFloatOrNull() ?: return Result.Error(
            errorMessage = UiText.StringResource(
                resId = R.string.error_weight_cant_be_empty
            )
        )

        if (weight.length > WEIGHT_MAX_LENGTH) {
            return Result.Error(
                errorMessage = UiText.StringResource(
                    resId = R.string.error_weight_exceeds_maximum_length
                )
            )
        }
        if (weight.length == WEIGHT_MAX_LENGTH) {
            if (weight.first().digitToInt() > Constants.WEIGHT_MAX_FIRST_DIGIT ||
                weight[SECOND_CHARACTER].digitToInt() > Constants.WEIGHT_MAX_SECOND_DIGIT
            ) {
                return Result.Error(
                    errorMessage = UiText.StringResource(
                        resId = R.string.error_weight_exceeds_human_capability
                    )
                )
            }
        }
        return Result.Success(weight = weightInNumber)
    }

    sealed class Result {
        data class Success(val weight: Float) : Result()
        data class Error(val errorMessage: UiText) : Result()
    }
}