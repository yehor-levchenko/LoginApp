package com.yehorlevchenko.data.storage.remote.api

import com.yehorlevchenko.domain.utils.ApiError
import com.yehorlevchenko.domain.utils.ApiResponse

class LoginApi {

    suspend fun login(username: String, password: String): ApiResponse<Int> {

        return when (username) {
            "user" -> ApiResponse(
                result = 1,
                error = null
            )
            "wrong" -> ApiResponse(
                result = null,
                error = ApiError.WRONG_CREDENTIALS
            )
            "internal" -> ApiResponse(
                result = null,
                error = ApiError.INTERNAL_SERVER_ERROR
            )
            else -> ApiResponse(
                result = null,
                error = ApiError.UNKNOWN_ERROR
            )
        }
    }
}