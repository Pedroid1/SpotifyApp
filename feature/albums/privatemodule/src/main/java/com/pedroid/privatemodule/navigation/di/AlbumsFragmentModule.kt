package com.pedroid.privatemodule.navigation.di

import com.pedroid.privatemodule.navigation.AlbumsFeatureCommunicatorImpl
import com.pedroid.publicmodule.AlbumsFeatureCommunicator
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent

@Module
@InstallIn(FragmentComponent::class)
interface AlbumsFragmentModule {

    @Binds
    fun bindCommunicator(registerFeatureCommunicatorImpl: AlbumsFeatureCommunicatorImpl): AlbumsFeatureCommunicator
}