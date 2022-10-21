package com.besirkaraoglu.livechat.data.di

import com.besirkaraoglu.livechat.analytics.AnalyticsSender
import com.besirkaraoglu.livechat.domain.MainAnalyticsInteractor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DomainModule {

    @Provides
    @Singleton
    fun provideMainAnalyticsInteractor(analyticsSender: AnalyticsSender) =
        MainAnalyticsInteractor(analyticsSender)
}