package com.pedroid.data.di

import com.pedroid.data.local.SecureStorage
import com.pedroid.data.local.SecureStorageImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SecureStorageModule {

    @Binds
    @Singleton
    abstract fun bindSecureStorage(
        impl: SecureStorageImpl
    ): SecureStorage
}
