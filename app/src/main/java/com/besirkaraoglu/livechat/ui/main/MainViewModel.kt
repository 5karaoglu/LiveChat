package com.besirkaraoglu.livechat.ui.main

import android.location.Location
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import com.besirkaraoglu.livechat.core.base.BaseViewModel
import com.besirkaraoglu.livechat.core.utils.Resource
import com.besirkaraoglu.livechat.data.model.LocationRecord
import com.besirkaraoglu.livechat.data.model.Users
import com.besirkaraoglu.livechat.data.repository.LocationRepository
import com.besirkaraoglu.livechat.data.repository.UsersRepository
import com.besirkaraoglu.livechat.domain.MainAnalyticsInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel
    @Inject constructor(
        private val analyticsInteractor: MainAnalyticsInteractor,
        private val locationRepository: LocationRepository,
        private val usersRepository: UsersRepository
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

    private val _upsertResult = MutableLiveData<Resource<LocationRecord>>()
    val upsertResult get() = _upsertResult

    private val _user = MutableLiveData<Resource<Users>>()
    val user get() = _user

    fun onFragmentCreated(){
        analyticsInteractor.startTracking()
    }

    fun sendData() {
        analyticsInteractor.sendEvent()
    }

    fun upsertLocationRecord(locationRecord: LocationRecord) = viewModelScope.launch {
        usersRepository.upsertActiveUser(locationRecord).collect{
            _upsertResult.value = it
        }
    }

    fun queryUser(uid:String) = viewModelScope.launch {
        usersRepository.queryUser(uid).collect{
            _user.value = it
        }
    }
}