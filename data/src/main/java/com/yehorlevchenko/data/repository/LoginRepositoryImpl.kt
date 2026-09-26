package com.yehorlevchenko.data.repository

import com.yehorlevchenko.core.annotations.IoDispatcher
import com.yehorlevchenko.data.storage.remote.datasource.RemoteLoginDataSource
import com.yehorlevchenko.domain.repository.LoginRepository
import com.yehorlevchenko.domain.utils.ApiResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val remoteLoginDataSource: RemoteLoginDataSource
) : LoginRepository {

    override suspend fun login(username: String, password: String): ApiResponse<Int> {
        return withContext(ioDispatcher) {
            remoteLoginDataSource.login(username, password)
        }
    }
}