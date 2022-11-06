package com.besirkaraoglu.livechat.core.utils

import android.content.*
import android.os.IBinder
import android.preference.PreferenceManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.besirkaraoglu.livechat.core.LocationReceiver
import com.besirkaraoglu.livechat.core.service.LocationService
import timber.log.Timber


interface LocationServiceHandler {
    fun setContext(context: Context)
    fun registerLifecycleOwnerForLocationService(lifecycleOwner: LifecycleOwner)
    val serviceConnection: ServiceConnection
    fun setReceiver(receiver: LocationReceiver)
    fun bindService()
    fun unbindService()
}

class LocationServiceHandlerImpl(
): LocationServiceHandler, LifecycleEventObserver, SharedPreferences.OnSharedPreferenceChangeListener{
    private var context: Context? = null

    private var mService: LocationService? = null

    // Tracks the bound state of the service.
    private var mBound = false

    private var mReceiver: LocationReceiver? = null

    override fun setContext(context: Context) {
        this.context = context
    }

    override fun registerLifecycleOwnerForLocationService(lifecycleOwner: LifecycleOwner) {
        lifecycleOwner.lifecycle.addObserver(this)
    }

    override val serviceConnection: ServiceConnection
        get() =  object : ServiceConnection{
            override fun onServiceConnected(p0: ComponentName?, service: IBinder?) {
                val binder: LocationService.LocalBinder = service as LocationService.LocalBinder
                mService = binder.service
                mBound = true
            }

            override fun onServiceDisconnected(p0: ComponentName?) {
                mService = null
                mBound = false
            }

            override fun onBindingDied(name: ComponentName?) {
                super.onBindingDied(name)
                mBound = false
            }

            override fun onNullBinding(name: ComponentName?) {
                super.onNullBinding(name)
                mBound = false
            }

        }

    override fun setReceiver(receiver: LocationReceiver) {
        mReceiver = receiver
    }

    override fun bindService() {
        // Bind to the service. If the service is in foreground mode, this signals to the service
        // that since this activity is in the foreground, the service can exit foreground mode.
        mBound = context!!.bindService(
            Intent(context, LocationService::class.java), serviceConnection,
            Context.BIND_AUTO_CREATE
        )
    }

    override fun unbindService() {
        if (mBound) {
            // Unbind from the service. This signals to the service that this activity is no longer
            // in the foreground, and the service can respond by promoting itself to a foreground
            // service.
            try {
                context!!.unbindService(serviceConnection)
            }catch (e:Exception){
                Timber.e(e.message)
            }
            mBound = false;
        }
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when(event){
            Lifecycle.Event.ON_CREATE -> {}
            Lifecycle.Event.ON_START -> {

            }
            Lifecycle.Event.ON_RESUME -> {
                /*context?.let {
                    LocalBroadcastManager.getInstance(it).registerReceiver(mReceiver!!,
                        IntentFilter(LocationService.ACTION_BROADCAST))
                }*/
            }
            Lifecycle.Event.ON_PAUSE -> {
                /*context?.let { LocalBroadcastManager.getInstance(it).unregisterReceiver(mReceiver!!) }*/
            }
            Lifecycle.Event.ON_STOP -> {
                unbindService()
            }
            Lifecycle.Event.ON_DESTROY -> {}
            else -> {}
        }
    }

    override fun onSharedPreferenceChanged(p0: SharedPreferences?, p1: String?) {

    }

}