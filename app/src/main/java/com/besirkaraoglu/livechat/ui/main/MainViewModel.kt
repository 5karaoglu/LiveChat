package com.besirkaraoglu.livechat.ui.main

import android.location.Location
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.besirkaraoglu.livechat.core.base.BaseViewModel
import com.besirkaraoglu.livechat.data.repository.LocationRepository
import com.besirkaraoglu.livechat.domain.MainAnalyticsInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel
    @Inject constructor(
        private val analyticsInteractor: MainAnalyticsInteractor,
        private val locationRepository: LocationRepository
    ): BaseViewModel() {

    private val locationObserver = Observer<Location>{
        _location.value = it
    }

    init {
      locationRepository.locationMediatorLiveData.observeForever(locationObserver)
    }

    override fun onCleared() {
        location.removeObserver(locationObserver)
        super.onCleared()
    }

    private val _location = MutableLiveData<Location>()
    val location get() = _location

    fun onFragmentCreated(){
        analyticsInteractor.startTracking()
    }

    fun sendData() {
        analyticsInteractor.sendEvent()
    }
}