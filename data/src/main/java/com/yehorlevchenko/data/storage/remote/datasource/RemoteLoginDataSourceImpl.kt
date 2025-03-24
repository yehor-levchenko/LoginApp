package com.yehorlevchenko.data.storage.remote.datasource

import com.yehorlevchenko.data.storage.remote.api.LoginApi
import com.yehorlevchenko.domain.utils.ApiResponse
import javax.inject.Inject

class RemoteLoginDataSourceImpl @Inject constructor(
    private val api: LoginApi
) : RemoteLoginDataSource {

    override suspend fun login(username: String, password: String): ApiResponse<Int> {
        return api.login(username, password)
    }
}