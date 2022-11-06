package com.besirkaraoglu.livechat.data.repository

import android.annotation.SuppressLint
import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.besirkaraoglu.livechat.core.utils.Resource
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import timber.log.Timber
import javax.inject.Inject

class LocationRepository
    @Inject constructor(
    private val fusedLocationProviderClient: FusedLocationProviderClient
) {


    val locationMediatorLiveData = MediatorLiveData<Location>()

    fun addLocationSource(liveData: LiveData<Location>){
        locationMediatorLiveData.addSource(liveData){
            locationMediatorLiveData.value = it
        }
    }

    @SuppressLint("MissingPermission")
    fun getLocation(): Flow<Resource<Location>> = channelFlow{
        send(Resource.Loading())
        fusedLocationProviderClient.lastLocation.addOnCompleteListener { result ->
            if (result.isSuccessful && result.result != null){
                Timber.tag("LocationResult").d("Location result succeed. ${result.result}")
                Resource.Success(result.result)
            }else{
                Timber.tag("LocationResult").e("Location result failed. ${result.exception?.message}")

            }
        }
    }
}