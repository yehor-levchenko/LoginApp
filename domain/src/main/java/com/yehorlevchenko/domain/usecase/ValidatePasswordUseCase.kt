package com.yehorlevchenko.domain.usecase

class ValidatePasswordUseCase {

     operator fun invoke(password: String): Boolean {
         return password.isNotEmpty()
     }
}