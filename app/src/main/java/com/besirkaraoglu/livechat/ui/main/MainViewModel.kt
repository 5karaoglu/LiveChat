package com.besirkaraoglu.livechat.ui.main

import com.besirkaraoglu.livechat.core.base.BaseViewModel
import com.besirkaraoglu.livechat.domain.MainAnalyticsInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel
    @Inject constructor(
        private val analyticsInteractor: MainAnalyticsInteractor
    ): BaseViewModel() {

    fun onFragmentCreated(){
        analyticsInteractor.startTracking()
    }

    fun sendData() {
        analyticsInteractor.sendEvent()
    }
}