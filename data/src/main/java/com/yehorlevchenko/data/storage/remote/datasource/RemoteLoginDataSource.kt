package com.yehorlevchenko.data.storage.remote.datasource

import com.yehorlevchenko.domain.utils.ApiResponse

interface RemoteLoginDataSource {

    suspend fun login(username: String, password: String): ApiResponse<Int>
}