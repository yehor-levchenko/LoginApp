package com.yehorlevchenko.data.di

import com.yehorlevchenko.data.storage.remote.datasource.RemoteLoginDataSource
import com.yehorlevchenko.data.storage.remote.datasource.RemoteLoginDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    @Binds
    @Singleton
    abstract fun bindRemoteLoginDataSource(
        remoteLoginDataSourceImpl: RemoteLoginDataSourceImpl
    ): RemoteLoginDataSource
}