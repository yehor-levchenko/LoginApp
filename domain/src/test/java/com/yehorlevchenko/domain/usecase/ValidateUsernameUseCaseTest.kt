package com.yehorlevchenko.domain.usecase

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ValidateUsernameUseCaseTest {

    private val validateUsernameUseCase = ValidateUsernameUseCase()

    @Test
    fun `when username is empty, validation fails`() {
        assertFalse(validateUsernameUseCase(""))
    }

    @Test
    fun `when username is not empty, validation passes`() {
        assertTrue(validateUsernameUseCase("user"))
    }
}
