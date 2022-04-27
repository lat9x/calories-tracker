package com.tuan.onboarding_presentation.height

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tuan.core.domain.preferences.Preferences
import com.tuan.core.domain.use_case.TakeOnlyDigits
import com.tuan.core.util.UiEvent
import com.tuan.onboarding_domain.use_case.OnboardingUseCases
import com.tuan.onboarding_domain.use_case.ValidateHeight
import com.tuan.onboarding_presentation.util.Constants.DEFAULT_HEIGHT
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HeightViewModel @Inject constructor(
    private val preferences: Preferences,
    private val takeOnlyDigits: TakeOnlyDigits,
    private val onboardingUseCases: OnboardingUseCases
) : ViewModel() {

    var height: String by mutableStateOf(value = DEFAULT_HEIGHT)
        private set

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent: Flow<UiEvent> = _uiEvent.receiveAsFlow()

    /**
     * Update user height, eliminate non-digit character
     *
     * @param height entered height
     */
    fun onHeightEnter(height: String) {
        this.height = takeOnlyDigits(text = height)
    }

    /**
     * Validate height, send error message if any error exists. Else, save the entered height and
     * send "move to next screen" event to the UI.
     */
    fun onNextClick() {
        when (val result: ValidateHeight.Result =
            onboardingUseCases.validateHeight(height = height)
        ) {
            is ValidateHeight.Result.Success -> {
                preferences.saveHeight(height = result.height)

                viewModelScope.launch {
                    _uiEvent.send(element = UiEvent.ToNextScreen)
                }
            }
            is ValidateHeight.Result.Error -> {
                viewModelScope.launch {
                    _uiEvent.send(
                        element = UiEvent.ShowSnackBar(
                            message = result.errorMessage
                        )
                    )
                }
            }
        }
    }
}