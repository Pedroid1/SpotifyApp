package com.pedroid.playlist.navigation.di

import com.pedroid.playlist.navigation.PlaylistNavigationNode
import com.pedroid.navigation.NavigationNode
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(SingletonComponent::class)
interface PlaylistNavigationModule {
    @IntoSet
    @Binds
    fun bindNavigationNode(playlistNavigationNode: PlaylistNavigationNode): NavigationNode
}
