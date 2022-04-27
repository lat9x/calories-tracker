package com.tuan.onboarding_presentation.goal

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tuan.core.domain.model.GoalType
import com.tuan.core.domain.preferences.Preferences
import com.tuan.core.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GoalViewModel @Inject constructor(
    private val preferences: Preferences
): ViewModel() {

    var selectedGoal: GoalType by mutableStateOf(value = GoalType.KeepWeight)
        private set

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    /**
     * Update the selected weight goal
     *
     * @param goalType selected weight goal
     */
    fun onGoalTypeSelect(goalType: GoalType) {
        selectedGoal = goalType
    }

    /**
     * Save the chosen weight goal and send "navigate to next screen" event to the UI
     */
    fun onNextClick() {
        viewModelScope.launch {
            preferences.saveGoalType(selectedGoal)
            _uiEvent.send(element = UiEvent.ToNextScreen)
        }
    }
}