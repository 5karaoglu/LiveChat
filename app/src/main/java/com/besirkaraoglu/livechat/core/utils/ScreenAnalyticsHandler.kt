package com.besirkaraoglu.livechat.core.utils

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner

interface ScreenAnalyticsHandler {
    fun registerLifecycleOwner(lifecycleOwner: LifecycleOwner)
}

class ScreenAnalyticsHandlerImpl : ScreenAnalyticsHandler, LifecycleEventObserver {
    override fun registerLifecycleOwner(lifecycleOwner: LifecycleOwner) {
        lifecycleOwner.lifecycle.addObserver(this)
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when (event) {
            Lifecycle.Event.ON_RESUME -> {

            }

            Lifecycle.Event.ON_PAUSE -> {

            }
            else -> Unit
        }
    }
}