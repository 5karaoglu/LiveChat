package com.besirkaraoglu.livechat.data.di

import com.besirkaraoglu.livechat.data.repository.AnalyticsRepository
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
    fun provideMainAnalyticsInteractor(analyticsRepository: AnalyticsRepository) =
        MainAnalyticsInteractor(analyticsRepository)
}