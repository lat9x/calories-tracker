package com.tuan.tracker_domain.model

sealed class MealType(val name: String) {
    object Breakfast : MealType(name = BREAKFAST)
    object Lunch : MealType(name = LUNCH)
    object Dinner : MealType(name = DINNER)
    object Snack : MealType(name = SNACK)

    companion object {
        private const val BREAKFAST = "breakfast"
        private const val LUNCH = "lunch"
        private const val DINNER = "dinner"
        private const val SNACK = "snack"


        fun fromString(name: String): MealType {
            return when (name) {
                BREAKFAST -> Breakfast
                LUNCH -> Lunch
                DINNER -> Dinner
                SNACK -> Snack
                else -> Breakfast
            }
        }
    }
}
