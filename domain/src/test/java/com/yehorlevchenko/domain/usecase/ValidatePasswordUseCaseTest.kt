package com.yehorlevchenko.domain.usecase

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ValidatePasswordUseCaseTest {

    private val validatePasswordUseCase = ValidatePasswordUseCase()

    @Test
    fun `when password is empty, validation fails`() {
        assertFalse(validatePasswordUseCase(""))
    }

    @Test
    fun `when password is not empty, validation passes`() {
        assertTrue(validatePasswordUseCase("password"))
    }
}
