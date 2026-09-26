package com.yehorlevchenko.data.storage.remote.api

import com.yehorlevchenko.domain.utils.ApiError
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class LoginApiTest {

    private val loginApi = LoginApi()

    @Test
    fun `when username is user, login succeeds`() = runTest {
        val response = loginApi.login("user", "password")

        assertEquals(1, response.result)
        assertNull(response.error)
    }

    @Test
    fun `when username is wrong, wrong credentials error is returned`() = runTest {
        val response = loginApi.login("wrong", "password")

        assertNull(response.result)
        assertEquals(ApiError.WRONG_CREDENTIALS, response.error)
    }

    @Test
    fun `when username is internal, internal server error is returned`() = runTest {
        val response = loginApi.login("internal", "password")

        assertNull(response.result)
        assertEquals(ApiError.INTERNAL_SERVER_ERROR, response.error)
    }

    @Test
    fun `when username is unknown, unknown error is returned`() = runTest {
        val response = loginApi.login("unknown", "password")

        assertNull(response.result)
        assertEquals(ApiError.UNKNOWN_ERROR, response.error)
    }
}
