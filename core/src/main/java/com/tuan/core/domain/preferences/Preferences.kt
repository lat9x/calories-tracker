package com.tuan.core.domain.preferences

import com.tuan.core.domain.model.ActivityLevel
import com.tuan.core.domain.model.Gender
import com.tuan.core.domain.model.GoalType
import com.tuan.core.domain.model.UserInfo

/**
 * Tại sao lại có Preferences ở trong module core?
 * Vì cả 2 module của app (onboarding và tracker) đều sử dụng đến nó.
 * - Trong module onboarding, Preferences để lưu thông tin.
 * - Trong module tracker, Preferences để lấy thông tin.
 */
interface Preferences {
    fun saveGender(gender: Gender)
    fun saveAge(age: Int)
    fun saveWeight(weight: Float)
    fun saveHeight(height: Int)
    fun saveActivityLevel(activityLevel: ActivityLevel)
    fun saveGoalType(goalType: GoalType)
    fun saveCarbRatio(carbRatio: Float)
    fun saveProteinRatio(proteinRatio: Float)
    fun saveFatRatio(fatRatio: Float)

    fun loadUserInfo(): UserInfo

    fun saveOnboardingShowState(isShown: Boolean)
    fun loadOnboardingShowState(): Boolean

    companion object {
        const val KEY_GENDER = "gender"
        const val KEY_AGE = "age"
        const val KEY_HEIGHT = "height"
        const val KEY_WEIGHT = "weight"
        const val KEY_ACTIVITY_LEVEL = "activity_level"
        const val KEY_GOAL_TYPE = "goal_type"
        const val KEY_CARB_RATIO = "carb_ratio"
        const val KEY_PROTEIN_RATIO = "protein_ratio"
        const val KEY_FAT_RATIO = "fat_ratio"
        const val KEY_SHOW_ONBOARDING = "show_onboarding"
    }
}