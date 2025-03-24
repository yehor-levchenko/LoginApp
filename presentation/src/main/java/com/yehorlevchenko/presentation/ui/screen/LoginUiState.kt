package com.yehorlevchenko.presentation.ui.screen

data class LoginUiState(
    val username: String = "",
    val password: String = "",
    val uiEvent: LoginEvent.UiEvent? = null
)