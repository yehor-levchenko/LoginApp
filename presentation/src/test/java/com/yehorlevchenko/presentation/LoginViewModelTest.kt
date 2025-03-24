package com.yehorlevchenko.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.yehorlevchenko.domain.usecase.LoginUseCase
import com.yehorlevchenko.domain.usecase.ValidatePasswordUseCase
import com.yehorlevchenko.domain.usecase.ValidateUsernameUseCase
import com.yehorlevchenko.presentation.entity.LoginMessage
import com.yehorlevchenko.presentation.ui.screen.LoginEvent
import com.yehorlevchenko.presentation.ui.screen.LoginViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private val loginUseCase: LoginUseCase = mock()
    private val validateUsernameUseCase: ValidateUsernameUseCase = mock()
    private val validatePasswordUseCase: ValidatePasswordUseCase = mock()

    private val viewModel = LoginViewModel(
        loginUseCase = loginUseCase,
        validateUsernameUseCase = validateUsernameUseCase,
        validatePasswordUseCase = validatePasswordUseCase
    )

    @Test
    fun `when username is empty, error message is shown`() = runTest {
        val username = ""
        val password = "password"

        whenever(validateUsernameUseCase.invoke(username)).thenReturn(false)

        viewModel.onUserEvent(LoginEvent.UserEvent.UsernameChanged(username))
        viewModel.onUserEvent(LoginEvent.UserEvent.PasswordChanged(password))
        viewModel.onUserEvent(LoginEvent.UserEvent.LoginClicked)

        val uiEvent = viewModel.uiState.firstOrNull()?.uiEvent
        assertNotNull(uiEvent)

        assertTrue(uiEvent is LoginEvent.UiEvent.ShowMessage)

        val message = (uiEvent as LoginEvent.UiEvent.ShowMessage).message
        assertTrue(message is LoginMessage.Error.InvalidUsername)
    }
}