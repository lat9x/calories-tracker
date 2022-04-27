package com.tuan.core.domain.model

import com.tuan.core.util.Constants.GAIN_WEIGHT
import com.tuan.core.util.Constants.KEEP_WEIGHT
import com.tuan.core.util.Constants.LOSE_WEIGHT

sealed class GoalType(val name: String) {
    object LoseWeight : GoalType(name = LOSE_WEIGHT)
    object KeepWeight : GoalType(name = KEEP_WEIGHT)
    object GainWeight : GoalType(name = GAIN_WEIGHT)

    companion object {

        /**
         * Convert string into GoalType object
         *
         * @param goalType name of the goal type
         * @return GoalType object
         */
        fun fromString(goalType: String): GoalType {
            return when (goalType) {
                LOSE_WEIGHT -> LoseWeight
                KEEP_WEIGHT -> KeepWeight
                GAIN_WEIGHT -> GainWeight
                else -> KeepWeight
            }
        }
    }
}
