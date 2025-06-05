package com.pedroid.data.di

import com.pedroid.data.repository.ArtistsRepositoryImpl
import com.pedroid.data.repository.ProfileRepositoryImpl
import com.pedroid.data.repository.auth.AuthRepository
import com.pedroid.data.repository.auth.AuthRepositoryImpl
import com.pedroid.domain.repository.ArtistsRepository
import com.pedroid.domain.repository.ProfileRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        impl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindArtistRepository(
        impl: ArtistsRepositoryImpl
    ): ArtistsRepository

    @Binds
    @Singleton
    abstract fun bindProfileRepository(
        impl: ProfileRepositoryImpl
    ): ProfileRepository
}
