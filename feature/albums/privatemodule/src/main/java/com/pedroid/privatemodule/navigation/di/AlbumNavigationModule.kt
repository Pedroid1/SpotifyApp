package com.pedroid.privatemodule.navigation.di

import com.pedroid.navigation.NavigationNode
import com.pedroid.privatemodule.navigation.AlbumNavigationNode
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(SingletonComponent::class)
interface AlbumNavigationModule {
    @IntoSet
    @Binds
    fun bindNavigationNode(albumNavigationNode: AlbumNavigationNode): NavigationNode
}
