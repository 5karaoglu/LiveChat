package com.besirkaraoglu.livechat.data.repository

import com.besirkaraoglu.livechat.analytics.AnalyticsEvent
import com.besirkaraoglu.livechat.analytics.AnalyticsProperty
import com.besirkaraoglu.livechat.analytics.AnalyticsProvider
import com.besirkaraoglu.livechat.analytics.toBundle
import com.google.firebase.analytics.FirebaseAnalytics
import timber.log.Timber

class AnalyticsRepository(
    private val firebaseAnalytics: FirebaseAnalytics
) {

    fun sendEvent(event: AnalyticsEvent) {
        if (event.providers.contains(AnalyticsProvider.ANALYTICS_FIREBASE)) {
            firebaseAnalytics.logEvent(event.eventName, event.params.toBundle())
        }
        Timber.tag("ArchAnalytics")
            .d("Event was sent: ${event.eventName}. Params: ${event.params}. Providers: ${event.providers}")
    }

    fun setUserProperty(property: AnalyticsProperty) {
        if (property.providers.contains(AnalyticsProvider.ANALYTICS_FIREBASE)) {
            firebaseAnalytics.setUserProperty(property.propertyName, property.toString())
        }

    }
}