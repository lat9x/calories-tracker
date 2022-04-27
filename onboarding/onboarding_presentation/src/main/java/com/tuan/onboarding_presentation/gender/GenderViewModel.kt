package com.tuan.onboarding_presentation.gender

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tuan.core.domain.model.Gender
import com.tuan.core.domain.preferences.Preferences
import com.tuan.core.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GenderViewModel @Inject constructor(
    private val preferences: Preferences
) : ViewModel() {

    var selectedGender: Gender by mutableStateOf(value = Gender.Male)
        private set

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent: Flow<UiEvent> = _uiEvent.receiveAsFlow()

    /**
     * Update the selected gender
     *
     * @param gender selected gender
     */
    fun onGenderClick(gender: Gender) {
        selectedGender = gender
    }

    /**
     * Save the chosen gender and send "navigate to next screen" event to the UI
     */
    fun onNextClick() {
        viewModelScope.launch {
            // save
            preferences.saveGender(gender = selectedGender)

            // navigate next screen
            _uiEvent.send(element = UiEvent.ToNextScreen)
        }
    }
}