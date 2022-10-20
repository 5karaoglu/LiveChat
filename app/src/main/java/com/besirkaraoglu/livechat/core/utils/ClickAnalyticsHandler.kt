package com.besirkaraoglu.livechat.core.utils

import android.view.View

interface ClickAnalyticsHandler {
    fun registerClickToAnalytics(view: View)
}

class ClickAnalyticsHandlerImpl : ClickAnalyticsHandler {

    override fun registerClickToAnalytics(view: View) {
        /**
         *This will enforce the rule to set the tag to all button that'll be sending analytics.
         */
        if (view.tag.toString().isEmpty()) {
            throw RuntimeException("Assign tag to asset ${view.id}")
        }

        //set event here
    }
}