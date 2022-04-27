package com.tuan.core.domain.model

import com.tuan.core.util.Constants.FEMALE
import com.tuan.core.util.Constants.MALE

sealed class Gender(val genderName: String) {
    object Male : Gender(genderName = MALE)
    object Female : Gender(genderName = FEMALE)

    companion object {

        /**
         * Convert string into Gender object
         *
         * @param genderName name of the gender
         * @return Gender object
         */
        fun fromString(genderName: String): Gender {
            return when (genderName) {
                MALE -> Male
                FEMALE -> Female
                else -> Male
            }
        }
    }
}
