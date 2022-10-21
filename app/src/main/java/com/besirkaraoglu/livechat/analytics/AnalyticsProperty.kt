package com.besirkaraoglu.livechat.analytics

abstract class AnalyticsProperty(
    val propertyName: String,
    val parameter: Any,
    val providers: List<AnalyticsProvider> = listOf(
        AnalyticsProvider.ANALYTICS_FIREBASE
    )
)