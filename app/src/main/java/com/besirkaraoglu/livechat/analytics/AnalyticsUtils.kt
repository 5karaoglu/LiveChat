package com.besirkaraoglu.livechat.analytics

import java.util.concurrent.TimeUnit

object AnalyticsUtils {
    fun getTimeDurationInSecBetweenNow(startTimeMillis: Long) =
        TimeUnit.SECONDS.convert(System.currentTimeMillis() - startTimeMillis, TimeUnit.MILLISECONDS)
}