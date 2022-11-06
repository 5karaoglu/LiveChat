package com.besirkaraoglu.livechat.core.service

import android.annotation.SuppressLint
import android.app.*
import android.app.Service.START_NOT_STICKY
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.content.res.Configuration
import android.location.Location
import android.os.*
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat.stopForeground
import androidx.lifecycle.MutableLiveData
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.test.core.app.ApplicationProvider
import com.besirkaraoglu.livechat.MainActivity
import com.google.android.gms.location.*
import com.google.android.gms.tasks.OnCompleteListener
import timber.log.Timber
import com.besirkaraoglu.livechat.R
import com.besirkaraoglu.livechat.core.utils.LocationUtils
import com.besirkaraoglu.livechat.core.utils.LocationUtils.getLocationText
import com.besirkaraoglu.livechat.core.utils.LocationUtils.getLocationTitle
import com.besirkaraoglu.livechat.data.repository.LocationRepository
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@SuppressLint("MissingPermission")
@AndroidEntryPoint
class LocationService : Service() {

    @Inject
    lateinit var locationRepository: LocationRepository

    /**
     * The current location.
     */
    private val _mLocation = MutableLiveData<Location>()
    val mLocation get() = _mLocation

    private val mBinder: IBinder = LocalBinder()

    /**
     * Used to check whether the bound activity has really gone away and not unbound as part of an
     * orientation change. We create a foreground service notification only if the former takes
     * place.
     */
    private var mChangingConfiguration = false
    private var mNotificationManager: NotificationManager? = null

    /**
     * Contains parameters used by [com.google.android.gms.location.FusedLocationProviderApi].
     */
    private var mLocationRequest: LocationRequest? = null

    /**
     * Provides access to the Fused Location Provider API.
     */
    private var mFusedLocationClient: FusedLocationProviderClient? = null

    /**
     * Callback for changes in location.
     */
    private var mLocationCallback: LocationCallback? = null
    private var mServiceHandler: Handler? = null

    override fun onCreate() {
        super.onCreate()
        locationRepository.addLocationSource(mLocation)
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        mLocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                onNewLocation(locationResult.lastLocation)
            }
        }
        createLocationRequest()
        lastLocation
        val handlerThread = HandlerThread(TAG)
        handlerThread.start()
        mServiceHandler = Handler(handlerThread.looper)
        mNotificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager?

        // Android O requires a Notification Channel.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name: CharSequence = getString(R.string.app_name)
            // Create the channel for the notification
            val mChannel =
                NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_DEFAULT)

            // Set the Notification Channel for the Notification Manager.
            mNotificationManager!!.createNotificationChannel(mChannel)
        }
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Timber.tag(TAG).i("Service started")
        val startedFromNotification = intent.getBooleanExtra(
            EXTRA_STARTED_FROM_NOTIFICATION,
            false
        )

        // We got here because the user decided to remove location updates from the notification.
        if (startedFromNotification) {
            removeLocationUpdates()
        }
        // Tells the system to not try to recreate the service after it has been killed.
        return START_NOT_STICKY
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        mChangingConfiguration = true
    }

    override fun onBind(intent: Intent?): IBinder {
        Timber.tag(TAG).i("in onBind()")
        stopForeground(true)
        mChangingConfiguration = false
        return mBinder
    }

    override fun onRebind(intent: Intent?) {
        Timber.tag(TAG).i("in onRebind()")
        stopForeground(true)
        mChangingConfiguration = false
        super.onRebind(intent)
    }

    override fun onUnbind(intent: Intent?): Boolean {
        Timber.tag(TAG).i("Last client unbound from service")

        if (!mChangingConfiguration && LocationUtils.requestingLocationUpdates(this)) {
            Timber.tag(TAG).i("Starting foreground service")
            startForeground(NOTIFICATION_ID, notification)
        }
        return true // Ensures onRebind() is called when a client re-binds.
    }

    override fun onDestroy() {
        mServiceHandler?.removeCallbacksAndMessages(null)
    }


    @SuppressLint("MissingPermission")
    fun requestLocationUpdates() {
        Timber.tag(TAG).i("Requesting location updates")
        LocationUtils.setRequestingLocationUpdates(this, true)
        startService(
            Intent(
                applicationContext,
                LocationService::class.java
            )
        )
        try {
            mFusedLocationClient!!.requestLocationUpdates(
                mLocationRequest!!,
                mLocationCallback!!, Looper.myLooper()
            )
        } catch (unlikely: SecurityException) {
            LocationUtils.setRequestingLocationUpdates(this, false)
            Timber.tag(TAG).e("Lost location permission. Could not request updates. $unlikely")
        }
    }


    private fun removeLocationUpdates() {
        Timber.tag(TAG).i("Removing location updates")
        try {
            mFusedLocationClient!!.removeLocationUpdates(mLocationCallback!!)
            LocationUtils.setRequestingLocationUpdates(this, false)
            stopSelf()
        } catch (e: Exception) {
            LocationUtils.setRequestingLocationUpdates(this, true)
            Timber.tag(TAG).e("Lost location permission. Could not remove updates. ${e.message}")
        }
    }// Channel ID// Extra to help us figure out if we arrived in onStartCommand via the notification or not.

    // The PendingIntent that leads to a call to onStartCommand() in this service.

    // The PendingIntent to launch activity.

    // Set the Channel ID for Android O.
    /**
     * Returns the [NotificationCompat] used as part of the foreground service.
     */
    private val notification: Notification
        get() {
            val intent = Intent(this, LocationService::class.java)
            val text: CharSequence = LocationUtils.getLocationText(mLocation.value)

            // Extra to help us figure out if we arrived in onStartCommand via the notification or not.
            intent.putExtra(EXTRA_STARTED_FROM_NOTIFICATION, true)

            // The PendingIntent that leads to a call to onStartCommand() in this service.
            val servicePendingIntent = PendingIntent.getService(
                this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )

            // The PendingIntent to launch activity.
            val activityPendingIntent = PendingIntent.getActivity(
                this, 0,
                Intent(this, MainActivity::class.java), 0
            )
            val builder: NotificationCompat.Builder = NotificationCompat.Builder(this)
                .addAction(
                    R.drawable.ic_launch, getString(R.string.launch_activity),
                    activityPendingIntent
                )
                .addAction(
                    R.drawable.ic_cancel, getString(R.string.remove_location_updates),
                    servicePendingIntent
                )
                .setContentText(text)
                .setContentTitle(LocationUtils.getLocationTitle(this))
                .setOngoing(true)
                .setPriority(Notification.PRIORITY_HIGH)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setTicker(text)
                .setWhen(System.currentTimeMillis())

            // Set the Channel ID for Android O.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                builder.setChannelId(CHANNEL_ID) // Channel ID
            }
            return builder.build()
        }

    private val lastLocation: Unit
        get() {
            try {
                mFusedLocationClient!!.lastLocation
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful && task.result != null &&
                            (System.currentTimeMillis() - task.result.time < 3600000) ) {
                            _mLocation.value = task.result
                        } else {
                            Timber.tag(TAG).e("Failed to get location.")
                            requestLocationUpdates()
                        }
                    }
            } catch (unlikely: SecurityException) {
                Timber.tag(TAG).e("Lost location permission.$unlikely")
            }
        }

    private fun onNewLocation(location: Location?) {
        Timber.tag(TAG).i("New location: $location")
        location?.let {  _mLocation.value = location!! }

        // Notify anyone listening for broadcasts about the new location.
        val intent = Intent(ACTION_BROADCAST)
        intent.putExtra(EXTRA_LOCATION, location)
        LocalBroadcastManager.getInstance(applicationContext)
            .sendBroadcast(intent)

        // Update notification content if running as a foreground service.
        if (serviceIsRunningInForeground(this)) {
            mNotificationManager!!.notify(
                NOTIFICATION_ID,
                notification
            )
        }
    }

    /**
     * Sets the location request parameters.
     */
    private fun createLocationRequest() {
        mLocationRequest = LocationRequest
            .Builder(Priority.PRIORITY_HIGH_ACCURACY,
                UPDATE_INTERVAL_IN_MILLISECONDS).build()
    }

    /**
     * Class used for the client Binder.  Since this service runs in the same process as its
     * clients, we don't need to deal with IPC.
     */
    inner class LocalBinder : Binder() {
        val service: LocationService
            get() = this@LocationService
    }

    /**
     * Returns true if this is a foreground service.
     *
     * @param context The [Context].
     */
    private fun serviceIsRunningInForeground(context: Context): Boolean {
        val manager = context.getSystemService(
            Context.ACTIVITY_SERVICE
        ) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if (javaClass.name == service.service.className) {
                if (service.foreground) {
                    return true
                }
            }
        }
        return false
    }

    companion object {
        private const val PACKAGE_NAME =
            "com.besirkaraoglu.livechat.core.service.locationservice"
        private val TAG = LocationService::class.java.simpleName

        /**
         * The name of the channel for notifications.
         */
        private const val CHANNEL_ID = "channel_01"
        const val ACTION_BROADCAST = "$PACKAGE_NAME.broadcast"
        const val EXTRA_LOCATION = "$PACKAGE_NAME.location"
        private const val EXTRA_STARTED_FROM_NOTIFICATION = PACKAGE_NAME +
                ".started_from_notification"

        /**
         * The desired interval for location updates. Inexact. Updates may be more or less frequent.
         */
        private const val UPDATE_INTERVAL_IN_MILLISECONDS: Long = 10000

        /**
         * The identifier for the notification displayed for the foreground service.
         */
        private const val NOTIFICATION_ID = 12345678
    }
}