package com.pedroid.eventbus.di

import com.pedroid.eventbus.EventBusController
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class EventBusModule {

    @Provides
    @Singleton
    fun provideEventBusController(): EventBusController {
        return EventBusController()
    }
}