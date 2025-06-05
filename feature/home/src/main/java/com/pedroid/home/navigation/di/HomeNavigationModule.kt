package com.pedroid.home.navigation.di

import com.pedroid.home.navigation.HomeNavigationNode
import com.pedroid.navigation.NavigationNode
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(SingletonComponent::class)
interface HomeNavigationModule {
    @IntoSet
    @Binds
    fun bindNavigationNode(homeNavigationNode: HomeNavigationNode): NavigationNode
}
