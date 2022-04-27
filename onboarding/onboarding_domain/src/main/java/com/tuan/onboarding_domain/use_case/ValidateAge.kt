package com.tuan.onboarding_domain.use_case

import com.tuan.core.R
import com.tuan.core.util.UiText
import com.tuan.onboarding_domain.util.Constants.AGE_MAX_FIRST_DIGIT
import com.tuan.onboarding_domain.util.Constants.AGE_MAX_LENGTH
import com.tuan.onboarding_domain.util.Constants.AGE_MAX_SECOND_DIGIT
import com.tuan.onboarding_domain.util.Constants.SECOND_CHARACTER

class ValidateAge {

    /**
     * Validate age text field
     *
     * @param age age in form of string
     */
    operator fun invoke(age: String): Result {
        val ageInNumber: Int = age.toIntOrNull() ?: return Result.Error(
            errorMessage = UiText.StringResource(
                resId = R.string.error_age_cant_be_empty
            )
        )

        if (age.length > AGE_MAX_LENGTH) {
            return Result.Error(
                errorMessage = UiText.StringResource(
                    resId = R.string.error_age_exceeds_maximum_length
                )
            )
        }
        if (age.length == AGE_MAX_LENGTH) {
            if (age.first().digitToInt() > AGE_MAX_FIRST_DIGIT ||
                age[SECOND_CHARACTER].digitToInt() > AGE_MAX_SECOND_DIGIT
            ) {
                return Result.Error(
                    errorMessage = UiText.StringResource(
                        resId = R.string.error_age_exceeds_human_capability
                    )
                )
            }
        }
        return Result.Success(age = ageInNumber)
    }

    sealed class Result {
        data class Success(val age: Int) : Result()
        data class Error(val errorMessage: UiText) : Result()
    }
}