package com.tuan.onboarding_presentation.weight

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tuan.core.domain.preferences.Preferences
import com.tuan.core.util.UiEvent
import com.tuan.onboarding_domain.use_case.OnboardingUseCases
import com.tuan.onboarding_domain.use_case.ValidateWeight
import com.tuan.onboarding_presentation.util.Constants.DEFAULT_WEIGHT
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeightViewModel @Inject constructor(
    private val preferences: Preferences,
    private val onboardingUseCases: OnboardingUseCases
) : ViewModel() {

    var weight: String by mutableStateOf(value = DEFAULT_WEIGHT)
        private set

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent: Flow<UiEvent> = _uiEvent.receiveAsFlow()

    /**
     * Update user weight
     *
     * @param weight entered weight
     */
    fun onWeightEnter(weight: String) {
        this.weight = weight
    }

    /**
     * Validate weight, send error message if any error exists. Else, save the entered weight and
     * send "move to next screen" event to the UI.
     */
    fun onNextClick() {

        when (val result: ValidateWeight.Result =
            onboardingUseCases.validateWeight(weight = weight)
        ) {
            is ValidateWeight.Result.Success -> {
                preferences.saveWeight(weight = result.weight)

                viewModelScope.launch {
                    _uiEvent.send(element = UiEvent.ToNextScreen)
                }
            }
            is ValidateWeight.Result.Error -> {
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