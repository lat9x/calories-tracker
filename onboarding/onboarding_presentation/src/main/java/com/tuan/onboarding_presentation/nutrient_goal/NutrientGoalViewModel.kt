package com.tuan.onboarding_presentation.nutrient_goal

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tuan.core.domain.preferences.Preferences
import com.tuan.core.domain.use_case.TakeOnlyDigits
import com.tuan.core.util.UiEvent
import com.tuan.onboarding_domain.use_case.OnboardingUseCases
import com.tuan.onboarding_domain.use_case.ValidateNutrients
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NutrientGoalViewModel @Inject constructor(
    private val preferences: Preferences,
    private val takeOnlyDigits: TakeOnlyDigits,
    private val onboardingUseCases: OnboardingUseCases
) : ViewModel() {

    var state: NutrientGoalState by mutableStateOf(value = NutrientGoalState())
        private set

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent: Flow<UiEvent> = _uiEvent.receiveAsFlow()

    fun onEvent(event: NutrientGoalEvent) {
        when (event) {
            is NutrientGoalEvent.OnCarbPercentageEnter -> {
                state = state.copy(
                    carbPercentage = takeOnlyDigits(text = event.carbPercentage)
                )
            }
            is NutrientGoalEvent.OnProteinPercentageEnter -> {
                state = state.copy(
                    proteinPercentage = takeOnlyDigits(text = event.proteinPercentage)
                )
            }
            is NutrientGoalEvent.OnFatPercentageEnter -> {
                state = state.copy(
                    fatPercentage = takeOnlyDigits(text = event.fatPercentage)
                )
            }
            is NutrientGoalEvent.OnNextClick -> {
                val result: ValidateNutrients.Result = onboardingUseCases.validateNutrients(
                    carbPercentageText = state.carbPercentage,
                    proteinPercentageText = state.proteinPercentage,
                    fatPercentageText = state.fatPercentage
                )
                when (result) {
                    is ValidateNutrients.Result.Success -> {

                        preferences.saveOnboardingShowState(isShown = false)

                        preferences.saveCarbRatio(carbRatio = result.carbRatio)
                        preferences.saveProteinRatio(proteinRatio = result.proteinRatio)
                        preferences.saveFatRatio(fatRatio = result.fatRatio)

                        viewModelScope.launch {
                            _uiEvent.send(
                                element = UiEvent.ToNextScreen
                            )
                        }
                    }
                    is ValidateNutrients.Result.Error -> {
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
    }
}