package com.yehorlevchenko.presentation.di

import com.yehorlevchenko.domain.repository.LoginRepository
import com.yehorlevchenko.domain.usecase.LoginUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    @Singleton
    fun provideLoginUseCase(loginRepository: LoginRepository): LoginUseCase {
        return LoginUseCase(loginRepository)
    }
}