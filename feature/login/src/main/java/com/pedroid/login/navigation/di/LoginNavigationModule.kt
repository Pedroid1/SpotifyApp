package com.pedroid.login.navigation.di

import com.pedroid.login.navigation.LoginNavigationNode
import com.pedroid.navigation.NavigationNode
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(SingletonComponent::class)
interface LoginNavigationModule {
    @IntoSet
    @Binds
    fun bindNavigationNode(loginNavigationNode: LoginNavigationNode): NavigationNode
}