package com.yehorlevchenko.domain.usecase

import com.yehorlevchenko.domain.repository.LoginRepository
import com.yehorlevchenko.domain.utils.ApiResponse

class LoginUseCase(private val loginRepository: LoginRepository) {

     suspend operator fun invoke(username: String, password: String): ApiResponse<Int> {
         return loginRepository.login(username, password)
     }
}