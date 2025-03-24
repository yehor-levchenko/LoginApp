package com.yehorlevchenko.presentation.ui.screen

import com.yehorlevchenko.presentation.entity.LoginMessage

sealed interface LoginEvent {

    sealed interface UserEvent {
        class UsernameChanged(val username: String) : UserEvent
        class PasswordChanged(val password: String) : UserEvent
        class UiEventPerformed(val uiEvent: UiEvent) : UserEvent
        data object LoginClicked : UserEvent
    }

    sealed interface UiEvent {
        class ShowMessage(val message: LoginMessage) : UiEvent
    }
}