package com.besirkaraoglu.livechat.core.base

import android.content.Context
import android.util.AttributeSet
import com.besirkaraoglu.livechat.core.utils.ClickAnalyticsHandler
import com.besirkaraoglu.livechat.core.utils.ClickAnalyticsHandlerImpl
import com.google.android.material.button.MaterialButton

class BaseMaterialButton(context: Context, attributeSet: AttributeSet) :
    MaterialButton(context, attributeSet), ClickAnalyticsHandler by ClickAnalyticsHandlerImpl() {

    override fun performClick(): Boolean {
        registerClickToAnalytics(this)
        return super.performClick()
    }
}