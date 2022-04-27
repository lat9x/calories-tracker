package com.tuan.onboarding_domain.use_case

import com.tuan.core.R
import com.tuan.core.util.UiText
import com.tuan.onboarding_domain.util.Constants

class ValidateHeight {

    /**
     * Validate height text field
     *
     * @param height height in form of string
     */
    operator fun invoke(height: String): Result {
        val heightInNumber: Int = height.toIntOrNull() ?: return Result.Error(
            errorMessage = UiText.StringResource(
                resId = R.string.error_height_cant_be_empty
            )
        )

        if (height.length > Constants.HEIGHT_MAX_LENGTH) {
            return Result.Error(
                errorMessage = UiText.StringResource(
                    resId = R.string.error_height_exceeds_maximum_length
                )
            )
        }
        if (height.length == Constants.HEIGHT_MAX_LENGTH) {
            if (height.first().digitToInt() > Constants.HEIGHT_MAX_FIRST_DIGIT ||
                height[Constants.SECOND_CHARACTER].digitToInt() > Constants.HEIGHT_MAX_SECOND_DIGIT
            ) {
                return Result.Error(
                    errorMessage = UiText.StringResource(
                        resId = R.string.error_height_exceeds_human_capability
                    )
                )
            }
        }
        return Result.Success(height = heightInNumber)
    }

    sealed class Result {
        data class Success(val height: Int) : Result()
        data class Error(val errorMessage: UiText) : Result()
    }
}