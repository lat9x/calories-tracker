package com.tuan.core.domain.model

import com.tuan.core.util.Constants.HIGH
import com.tuan.core.util.Constants.LOW
import com.tuan.core.util.Constants.MEDIUM

sealed class ActivityLevel(val name: String) {
    object Low : ActivityLevel(name = LOW)
    object Medium : ActivityLevel(name = MEDIUM)
    object High : ActivityLevel(name = HIGH)

    companion object {

        /**
         * Convert string into ActivityLevel object
         *
         * @param activityLevel name of the activity level
         * @return ActivityLevel object
         */
        fun fromString(activityLevel: String): ActivityLevel {
            return when (activityLevel) {
                LOW -> Low
                MEDIUM -> Medium
                HIGH -> High
                else -> Medium
            }
        }
    }
}
