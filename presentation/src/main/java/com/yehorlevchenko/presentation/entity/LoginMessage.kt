package com.yehorlevchenko.presentation.entity

import androidx.annotation.StringRes
import com.yehorlevchenko.presentation.R

sealed class LoginMessage(@StringRes val messageResId: Int) {

    sealed class Success(@StringRes messageResId: Int) : LoginMessage(messageResId) {
        data object LoginSuccess : Success(R.string.message_successful_login)
    }

    sealed class Error(@StringRes messageResId: Int) : LoginMessage(messageResId) {
        data object InvalidUsername : Error(R.string.message_invalid_username)
        data object InvalidPassword : Error(R.string.message_invalid_password)
        data class NetworkError(@StringRes val errorMessageResId: Int) : Error(errorMessageResId)
    }
}