package com.tuan.core.data.preferences

import android.content.SharedPreferences
import com.tuan.core.domain.model.ActivityLevel
import com.tuan.core.domain.model.Gender
import com.tuan.core.domain.model.GoalType
import com.tuan.core.domain.model.UserInfo
import com.tuan.core.domain.preferences.Preferences
import com.tuan.core.util.Constants.DEFAULT_FLOAT
import com.tuan.core.util.Constants.DEFAULT_INT
import com.tuan.core.util.Constants.KEEP_WEIGHT
import com.tuan.core.util.Constants.MALE
import com.tuan.core.util.Constants.MEDIUM

class DefaultPreferences(
    private val sharedPref: SharedPreferences
) : Preferences {

    override fun saveGender(gender: Gender) {
        sharedPref.edit()
            .putString(Preferences.KEY_GENDER, gender.genderName)
            .apply()
    }

    override fun saveAge(age: Int) {
        sharedPref.edit()
            .putInt(Preferences.KEY_AGE, age)
            .apply()
    }

    override fun saveWeight(weight: Float) {
        sharedPref.edit()
            .putFloat(Preferences.KEY_WEIGHT, weight)
            .apply()
    }

    override fun saveHeight(height: Int) {
        sharedPref.edit()
            .putInt(Preferences.KEY_HEIGHT, height)
            .apply()
    }

    override fun saveActivityLevel(activityLevel: ActivityLevel) {
        sharedPref.edit()
            .putString(Preferences.KEY_ACTIVITY_LEVEL, activityLevel.name)
            .apply()
    }

    override fun saveGoalType(goalType: GoalType) {
        sharedPref.edit()
            .putString(Preferences.KEY_GOAL_TYPE, goalType.name)
            .apply()
    }

    override fun saveCarbRatio(carbRatio: Float) {
        sharedPref.edit()
            .putFloat(Preferences.KEY_CARB_RATIO, carbRatio)
            .apply()
    }

    override fun saveProteinRatio(proteinRatio: Float) {
        sharedPref.edit()
            .putFloat(Preferences.KEY_PROTEIN_RATIO, proteinRatio)
            .apply()
    }

    override fun saveFatRatio(fatRatio: Float) {
        sharedPref.edit()
            .putFloat(Preferences.KEY_FAT_RATIO, fatRatio)
            .apply()
    }

    override fun loadUserInfo(): UserInfo {
        val age = sharedPref.getInt(Preferences.KEY_AGE, DEFAULT_INT)
        val height = sharedPref.getInt(Preferences.KEY_HEIGHT, DEFAULT_INT)
        val weight = sharedPref.getFloat(Preferences.KEY_WEIGHT, DEFAULT_FLOAT)
        val genderName = sharedPref.getString(Preferences.KEY_GENDER, null)
        val activityLevelString = sharedPref.getString(Preferences.KEY_ACTIVITY_LEVEL, null)
        val goalTypeString = sharedPref.getString(Preferences.KEY_GOAL_TYPE, null)
        val carbRatio = sharedPref.getFloat(Preferences.KEY_CARB_RATIO, DEFAULT_FLOAT)
        val proteinRatio = sharedPref.getFloat(Preferences.KEY_PROTEIN_RATIO, DEFAULT_FLOAT)
        val fatRatio = sharedPref.getFloat(Preferences.KEY_FAT_RATIO, DEFAULT_FLOAT)

        return UserInfo(
            gender = Gender.fromString(
                genderName = genderName ?: MALE
            ),
            age = age,
            height = height,
            weight = weight,
            activityLevel = ActivityLevel.fromString(
                activityLevel = activityLevelString ?: MEDIUM
            ),
            goalType = GoalType.fromString(
                goalType = goalTypeString ?: KEEP_WEIGHT
            ),
            carbRatio = carbRatio,
            proteinRatio = proteinRatio,
            fatRatio = fatRatio
        )
    }

    override fun saveOnboardingShowState(isShown: Boolean) {
        sharedPref.edit()
            .putBoolean(Preferences.KEY_SHOW_ONBOARDING, isShown)
            .apply()
    }

    override fun loadOnboardingShowState(): Boolean {
        return sharedPref.getBoolean(Preferences.KEY_SHOW_ONBOARDING, true)
    }
}