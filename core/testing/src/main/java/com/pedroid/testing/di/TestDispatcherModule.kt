package com.pedroid.testing.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import javax.inject.Singleton

@OptIn(ExperimentalCoroutinesApi::class)
@Module
@InstallIn(SingletonComponent::class)
internal object TestDispatcherModule {
    @Provides
    @Singleton
    fun providesTestDispatcher(): TestDispatcher = UnconfinedTestDispatcher()
}
