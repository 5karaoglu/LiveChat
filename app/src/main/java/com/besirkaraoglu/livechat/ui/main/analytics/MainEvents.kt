package com.besirkaraoglu.livechat.ui.main.analytics

import com.besirkaraoglu.livechat.analytics.AnalyticsConstants
import com.besirkaraoglu.livechat.analytics.AnalyticsEvent

object MainEvents {

    class SendClicked(
        timeBetweenClicks: Long
    ) : AnalyticsEvent(
        AnalyticsConstants.Events.SendClicked.EVENT,
        mapOf( // create a map of properties where key is taken from constants, and value from
            AnalyticsConstants.Events.SendClicked.Params.TIME_BETWEEN_CLICKS to timeBetweenClicks
        )
    )
}