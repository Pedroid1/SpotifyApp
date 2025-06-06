package com.pedroid.analytics.di

import android.annotation.SuppressLint
import android.content.Context
import com.google.firebase.analytics.FirebaseAnalytics
import com.pedroid.analytics.FirebaseAnalyticsEventLogger
import com.pedroid.analytics.IAnalyticsEventLogger
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {

    @SuppressLint("MissingPermission")
    @Provides
    @Singleton
    fun provideFirebaseAnalytics(@ApplicationContext context: Context): FirebaseAnalytics {
        return FirebaseAnalytics.getInstance(context)
    }

    @Provides
    @Singleton
    fun provideAnalyticsEventLogger(
        firebaseAnalytics: FirebaseAnalytics
    ): IAnalyticsEventLogger {
        return FirebaseAnalyticsEventLogger(firebaseAnalytics)
    }
}
