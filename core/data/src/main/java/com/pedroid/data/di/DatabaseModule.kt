package com.pedroid.data.di

import android.content.Context
import androidx.room.Room
import com.pedroid.data.local.db.AppRoomDataBase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, AppRoomDataBase::class.java, "app.db")
            .fallbackToDestructiveMigration().build()
}
