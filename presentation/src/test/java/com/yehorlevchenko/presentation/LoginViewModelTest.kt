package com.yehorlevchenko.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.yehorlevchenko.domain.usecase.LoginUseCase
import com.yehorlevchenko.domain.usecase.ValidatePasswordUseCase
import com.yehorlevchenko.domain.usecase.ValidateUsernameUseCase
import com.yehorlevchenko.domain.utils.ApiError
import com.yehorlevchenko.domain.utils.ApiResponse
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
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
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

    @Test
    fun `when password is empty, error message is shown`() = runTest {
        val username = "user"
        val password = ""

        whenever(validateUsernameUseCase.invoke(username)).thenReturn(true)
        whenever(validatePasswordUseCase.invoke(password)).thenReturn(false)

        viewModel.onUserEvent(LoginEvent.UserEvent.UsernameChanged(username))
        viewModel.onUserEvent(LoginEvent.UserEvent.PasswordChanged(password))
        viewModel.onUserEvent(LoginEvent.UserEvent.LoginClicked)

        val message = getShownMessage()
        assertTrue(message is LoginMessage.Error.InvalidPassword)
        verify(loginUseCase, never()).invoke(any(), any())
    }

    @Test
    fun `when login succeeds, success message is shown`() = runTest {
        val username = "user"
        val password = "password"

        whenever(validateUsernameUseCase.invoke(username)).thenReturn(true)
        whenever(validatePasswordUseCase.invoke(password)).thenReturn(true)
        whenever(loginUseCase.invoke(username, password)).thenReturn(
            ApiResponse(result = 1, error = null)
        )

        viewModel.onUserEvent(LoginEvent.UserEvent.UsernameChanged(username))
        viewModel.onUserEvent(LoginEvent.UserEvent.PasswordChanged(password))
        viewModel.onUserEvent(LoginEvent.UserEvent.LoginClicked)

        val message = getShownMessage()
        assertTrue(message is LoginMessage.Success.LoginSuccess)
    }

    @Test
    fun `when login fails with wrong credentials, error message is shown`() = runTest {
        val username = "wrong"
        val password = "password"

        whenever(validateUsernameUseCase.invoke(username)).thenReturn(true)
        whenever(validatePasswordUseCase.invoke(password)).thenReturn(true)
        whenever(loginUseCase.invoke(username, password)).thenReturn(
            ApiResponse(result = null, error = ApiError.WRONG_CREDENTIALS)
        )

        viewModel.onUserEvent(LoginEvent.UserEvent.UsernameChanged(username))
        viewModel.onUserEvent(LoginEvent.UserEvent.PasswordChanged(password))
        viewModel.onUserEvent(LoginEvent.UserEvent.LoginClicked)

        val message = getShownMessage()
        assertEquals(
            LoginMessage.Error.NetworkError(R.string.message_wrong_credentials),
            message
        )
    }

    @Test
    fun `when ui event is performed, it is removed from ui state`() = runTest {
        val username = ""

        whenever(validateUsernameUseCase.invoke(username)).thenReturn(false)

        viewModel.onUserEvent(LoginEvent.UserEvent.LoginClicked)

        val uiEvent = viewModel.uiState.firstOrNull()?.uiEvent
        assertNotNull(uiEvent)

        viewModel.onUserEvent(LoginEvent.UserEvent.UiEventPerformed(uiEvent!!))

        assertNull(viewModel.uiState.firstOrNull()?.uiEvent)
    }

    private suspend fun getShownMessage(): LoginMessage {
        val uiEvent = viewModel.uiState.firstOrNull()?.uiEvent
        assertNotNull(uiEvent)
        assertTrue(uiEvent is LoginEvent.UiEvent.ShowMessage)

        return (uiEvent as LoginEvent.UiEvent.ShowMessage).message
    }
}
