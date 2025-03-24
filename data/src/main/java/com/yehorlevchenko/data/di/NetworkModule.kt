package com.yehorlevchenko.data.di

import com.yehorlevchenko.data.storage.remote.api.LoginApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideLoginApi(): LoginApi {
        return LoginApi()
    }
}