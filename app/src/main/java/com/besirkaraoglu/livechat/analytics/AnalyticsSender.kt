package com.besirkaraoglu.livechat.analytics

import com.google.firebase.analytics.FirebaseAnalytics
import timber.log.Timber

class AnalyticsSender(
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