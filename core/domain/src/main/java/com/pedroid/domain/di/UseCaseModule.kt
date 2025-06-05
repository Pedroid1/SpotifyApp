package com.pedroid.domain.di

import com.pedroid.domain.usecase.user.GetUserProfileUseCase
import com.pedroid.domain.usecase.user.GetUserProfileUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class UseCaseModule {

    @Binds
    abstract fun bindGetUserProfileUseCase(
        impl: GetUserProfileUseCaseImpl
    ): GetUserProfileUseCase
}