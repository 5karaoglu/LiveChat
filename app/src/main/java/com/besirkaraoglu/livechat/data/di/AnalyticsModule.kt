package com.besirkaraoglu.livechat.data.di

import android.annotation.SuppressLint
import android.content.Context
import com.besirkaraoglu.livechat.data.repository.AnalyticsRepository
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AnalyticsModule {

    @SuppressLint("MissingPermission")
    @Provides
    @Singleton
    fun provideFirebaseAnalytics(@ApplicationContext context: Context) =
        FirebaseAnalytics.getInstance(context)

    @Provides
    @Singleton
    fun provideAnalyticsRepository(firebaseAnalytics: FirebaseAnalytics) =
        AnalyticsRepository(firebaseAnalytics)
}