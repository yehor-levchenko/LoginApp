package com.yehorlevchenko.presentation.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.yehorlevchenko.presentation.R
import com.yehorlevchenko.presentation.ui.theme.padding01
import com.yehorlevchenko.presentation.ui.theme.spacer01

@Composable
fun LoginScreen(viewModel: LoginViewModel = hiltViewModel()) {
    val viewModelUiState = viewModel.uiState.collectAsState()
    val snackBarHostState = remember { SnackbarHostState() }

    Scaffold(snackbarHost = { SnackbarHost(hostState = snackBarHostState) }) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(padding01),
            verticalArrangement = Arrangement.spacedBy(spacer01)
        ) {
            TextField(
                value = viewModelUiState.value.username,
                onValueChange = { viewModel.onUserEvent(LoginEvent.UserEvent.UsernameChanged(it)) },
                label = { Text(stringResource(R.string.label_username)) },
                modifier = Modifier.fillMaxWidth()
            )

            TextField(
                value = viewModelUiState.value.password,
                onValueChange = { viewModel.onUserEvent(LoginEvent.UserEvent.PasswordChanged(it)) },
                label = { Text(stringResource(R.string.label_password)) },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = { viewModel.onUserEvent(LoginEvent.UserEvent.LoginClicked) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.label_login))
            }

            viewModelUiState.value.uiEvent?.let { uiEvent ->
                when (uiEvent) {
                    is LoginEvent.UiEvent.ShowMessage -> {
                        val message =  stringResource(uiEvent.message.messageResId)
                        LaunchedEffect(uiEvent) {
                            snackBarHostState.showSnackbar(
                                message = message,
                                duration = SnackbarDuration.Short
                            )
                            viewModel.onUserEvent(LoginEvent.UserEvent.UiEventPerformed(uiEvent))
                        }
                    }
                }
            }
        }
    }
}