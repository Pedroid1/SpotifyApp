package com.pedroid.profile.navigation.di

import com.pedroid.navigation.NavigationNode
import com.pedroid.profile.navigation.ProfileNavigationNode
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(SingletonComponent::class)
interface ProfileNavigationModule {
    @IntoSet
    @Binds
    fun bindNavigationNode(profileNavigationNode: ProfileNavigationNode): NavigationNode
}
