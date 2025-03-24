package com.yehorlevchenko.domain.usecase

class ValidateUsernameUseCase {

     operator fun invoke(username: String): Boolean {
         return username.isNotEmpty()
     }
}