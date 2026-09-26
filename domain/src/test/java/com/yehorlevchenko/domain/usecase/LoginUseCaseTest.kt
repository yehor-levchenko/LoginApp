package com.yehorlevchenko.domain.usecase

import com.yehorlevchenko.domain.repository.LoginRepository
import com.yehorlevchenko.domain.utils.ApiError
import com.yehorlevchenko.domain.utils.ApiResponse
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class LoginUseCaseTest {

    private val loginRepository: LoginRepository = mock()

    private val loginUseCase = LoginUseCase(loginRepository)

    @Test
    fun `when login succeeds, repository response is returned`() = runTest {
        val username = "user"
        val password = "password"
        val response = ApiResponse(result = 1, error = null)

        whenever(loginRepository.login(username, password)).thenReturn(response)

        val result = loginUseCase(username, password)

        verify(loginRepository).login(username, password)
        assertEquals(response, result)
    }

    @Test
    fun `when login fails, repository error is returned`() = runTest {
        val username = "wrong"
        val password = "password"
        val response = ApiResponse<Int>(result = null, error = ApiError.WRONG_CREDENTIALS)

        whenever(loginRepository.login(username, password)).thenReturn(response)

        val result = loginUseCase(username, password)

        assertEquals(response, result)
    }
}
