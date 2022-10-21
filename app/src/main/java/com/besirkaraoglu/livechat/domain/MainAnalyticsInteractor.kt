package com.besirkaraoglu.livechat.domain

import com.besirkaraoglu.livechat.analytics.AnalyticsSender
import com.besirkaraoglu.livechat.analytics.AnalyticsUtils
import com.besirkaraoglu.livechat.ui.main.analytics.MainEvents
import javax.inject.Inject

class MainAnalyticsInteractor
    @Inject constructor(
    private val analyticsSender: AnalyticsSender
){
    private var startTime: Long = 0

    fun startTracking() {
        startTime = System.currentTimeMillis()
    }

    fun sendEvent() {
        analyticsSender.sendEvent(MainEvents.SendClicked(AnalyticsUtils.getTimeDurationInSecBetweenNow(startTime)))
        startTracking()
    }
}