package com.tuan.core.util

sealed class UiEvent {
    object ToNextScreen : UiEvent()
    object NavigateUp : UiEvent()
    data class ShowSnackBar(val message: UiText) : UiEvent()
}