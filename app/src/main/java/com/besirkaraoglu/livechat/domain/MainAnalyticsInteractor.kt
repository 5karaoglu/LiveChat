package com.besirkaraoglu.livechat.domain

import com.besirkaraoglu.livechat.data.repository.AnalyticsRepository
import com.besirkaraoglu.livechat.analytics.AnalyticsUtils
import com.besirkaraoglu.livechat.ui.main.analytics.MainEvents
import javax.inject.Inject

class MainAnalyticsInteractor
    @Inject constructor(
    private val analyticsRepository: AnalyticsRepository
){
    private var startTime: Long = 0

    fun startTracking() {
        startTime = System.currentTimeMillis()
    }

    fun sendEvent() {
        analyticsRepository.sendEvent(MainEvents.SendClicked(AnalyticsUtils.getTimeDurationInSecBetweenNow(startTime)))
        startTracking()
    }
}