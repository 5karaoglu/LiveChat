package com.besirkaraoglu.livechat.core

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class LCApplication: Application() {
    override fun onCreate() {
        super.onCreate()
    }
}