package com.tuan.onboarding_presentation.age

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tuan.core.domain.preferences.Preferences
import com.tuan.core.domain.use_case.TakeOnlyDigits
import com.tuan.core.util.UiEvent
import com.tuan.onboarding_domain.use_case.OnboardingUseCases
import com.tuan.onboarding_domain.use_case.ValidateAge
import com.tuan.onboarding_presentation.util.Constants.DEFAULT_AGE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AgeViewModel @Inject constructor(
    private val preferences: Preferences,
    private val takeOnlyDigits: TakeOnlyDigits,
    private val onboardingUseCases: OnboardingUseCases
) : ViewModel() {

    var age by mutableStateOf(value = DEFAULT_AGE)
        private set

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent: Flow<UiEvent> = _uiEvent.receiveAsFlow()

    /**
     * Update user age, take only digits
     *
     * @param age entered age
     */
    fun onAgeEnter(age: String) {
        this.age = takeOnlyDigits(text = age)
    }

    /**
     * Validate age, send error message if any error exists. Else, save the entered age and
     * send "move to next screen" event to the UI.
     */
    fun onNextClick() {

        when (val result: ValidateAge.Result = onboardingUseCases.validateAge(age = age)) {
            is ValidateAge.Result.Success -> {
                preferences.saveAge(age = result.age)

                viewModelScope.launch {
                    _uiEvent.send(element = UiEvent.ToNextScreen)
                }
            }
            is ValidateAge.Result.Error -> {
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