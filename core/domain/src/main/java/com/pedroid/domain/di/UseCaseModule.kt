package com.pedroid.domain.di

import com.pedroid.domain.usecase.albums.GetAlbumsUseCase
import com.pedroid.domain.usecase.albums.GetAlbumsUseCaseImpl
import com.pedroid.domain.usecase.artist.GetArtistsUseCase
import com.pedroid.domain.usecase.artist.GetArtistsUseCaseImpl
import com.pedroid.domain.usecase.playlist.CreatePlaylistUseCase
import com.pedroid.domain.usecase.playlist.CreatePlaylistUseCaseImpl
import com.pedroid.domain.usecase.playlist.GetPlaylistsUseCase
import com.pedroid.domain.usecase.playlist.GetPlaylistsUseCaseImpl
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

    @Binds
    abstract fun bindGetPlaylistsUseCase(
        impl: GetPlaylistsUseCaseImpl
    ): GetPlaylistsUseCase

    @Binds
    abstract fun bindGetArtistsUseCase(
        impl: GetArtistsUseCaseImpl
    ): GetArtistsUseCase

    @Binds
    abstract fun bindGetCreatePlaylistUseCase(
        impl: CreatePlaylistUseCaseImpl
    ): CreatePlaylistUseCase

    @Binds
    abstract fun bindGetAlbumsUseCase(
        impl: GetAlbumsUseCaseImpl
    ): GetAlbumsUseCase
}