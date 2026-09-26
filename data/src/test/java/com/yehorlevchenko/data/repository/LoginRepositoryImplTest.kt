package com.yehorlevchenko.data.repository

import com.yehorlevchenko.data.storage.remote.datasource.RemoteLoginDataSource
import com.yehorlevchenko.domain.utils.ApiError
import com.yehorlevchenko.domain.utils.ApiResponse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class LoginRepositoryImplTest {

    private val testDispatcher = UnconfinedTestDispatcher()

    private val remoteLoginDataSource: RemoteLoginDataSource = mock()

    private val loginRepository = LoginRepositoryImpl(
        ioDispatcher = testDispatcher,
        remoteLoginDataSource = remoteLoginDataSource
    )

    @Test
    fun `when login succeeds, data source response is returned`() = runTest(testDispatcher) {
        val username = "user"
        val password = "password"
        val response = ApiResponse(result = 1, error = null)

        whenever(remoteLoginDataSource.login(username, password)).thenReturn(response)

        val result = loginRepository.login(username, password)

        verify(remoteLoginDataSource).login(username, password)
        assertEquals(response, result)
    }

    @Test
    fun `when login fails, data source error is returned`() = runTest(testDispatcher) {
        val username = "internal"
        val password = "password"
        val response = ApiResponse<Int>(result = null, error = ApiError.INTERNAL_SERVER_ERROR)

        whenever(remoteLoginDataSource.login(username, password)).thenReturn(response)

        val result = loginRepository.login(username, password)

        assertEquals(response, result)
    }
}
