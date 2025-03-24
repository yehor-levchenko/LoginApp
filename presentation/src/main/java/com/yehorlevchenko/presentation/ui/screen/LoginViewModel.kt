package com.yehorlevchenko.presentation.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yehorlevchenko.domain.usecase.LoginUseCase
import com.yehorlevchenko.domain.usecase.ValidatePasswordUseCase
import com.yehorlevchenko.domain.usecase.ValidateUsernameUseCase
import com.yehorlevchenko.domain.utils.ApiError
import com.yehorlevchenko.presentation.R
import com.yehorlevchenko.presentation.entity.LoginMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val validateUsernameUseCase: ValidateUsernameUseCase,
    private val validatePasswordUseCase: ValidatePasswordUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState = _uiState.asStateFlow()

    private var uiEvents = mutableListOf<LoginEvent.UiEvent>()

    fun onUserEvent(event: LoginEvent.UserEvent) = viewModelScope.launch {
        when (event) {
            is LoginEvent.UserEvent.UsernameChanged -> handleUsernameChanged(event.username)
            is LoginEvent.UserEvent.PasswordChanged -> handlePasswordChanged(event.password)
            is LoginEvent.UserEvent.UiEventPerformed -> handleUiEventPerformed(event.uiEvent)
            is LoginEvent.UserEvent.LoginClicked -> handleLoginClicked()
        }
    }

    private fun handleUsernameChanged(username: String) {
        updateUiState(username = username)
    }

    private fun handlePasswordChanged(password: String) {
        updateUiState(password = password)
    }

    private suspend fun handleLoginClicked() {
        val username = getUiState().username
        val password = getUiState().password

        if (validateUsernameUseCase(username).not()) {
            val message = LoginMessage.Error.InvalidUsername
            updateUiState(uiEvent = LoginEvent.UiEvent.ShowMessage(message))
        } else if (validatePasswordUseCase(password).not()) {
            val message = LoginMessage.Error.InvalidPassword
            updateUiState(uiEvent = LoginEvent.UiEvent.ShowMessage(message))
        } else {
            performLogin(username, password)
        }
    }

    private suspend fun performLogin(username: String, password: String) {
        val response = loginUseCase(username, password)
        response.error?.let {
            val message = mapApiErrorToLoginMessage(it)
            updateUiState(uiEvent = LoginEvent.UiEvent.ShowMessage(message))
        } ?: run {
            val message = LoginMessage.Success.LoginSuccess
            updateUiState(uiEvent = LoginEvent.UiEvent.ShowMessage(message))
        }
    }

    private fun mapApiErrorToLoginMessage(error: ApiError): LoginMessage.Error {
        val errorMessageResId = when (error) {
            ApiError.WRONG_CREDENTIALS -> R.string.message_wrong_credentials
            ApiError.INTERNAL_SERVER_ERROR -> R.string.message_internal_server_error
            ApiError.UNKNOWN_ERROR -> R.string.message_unknown_error
        }
        return LoginMessage.Error.NetworkError(errorMessageResId)
    }

    private fun handleUiEventPerformed(uiEvent: LoginEvent.UiEvent) {
        uiEvents = uiEvents.apply { remove(uiEvent) }
        updateUiState()
    }

    private fun getUiState() = _uiState.value

    private fun updateUiState(
        username: String? = null,
        password: String? = null,
        uiEvent: LoginEvent.UiEvent? = null
    ) {
        uiEvent?.let { uiEvents += uiEvent }

        _uiState.update { previousState ->
            previousState.copy(
                username = username ?: previousState.username,
                password = password ?: previousState.password,
                uiEvent = uiEvents.firstOrNull()
            )
        }
    }
}