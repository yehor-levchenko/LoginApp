package com.yehorlevchenko.domain.repository

import com.yehorlevchenko.domain.utils.ApiResponse

interface LoginRepository {

    suspend fun login(username: String, password: String): ApiResponse<Int>
}