package com.besirkaraoglu.livechat.analytics

object UserProperties {
    class NotificationProperty(
        state: Boolean
    ) : AnalyticsProperty(AnalyticsConstants.UserProperties.NOTIFICATION_STATE, state)
}