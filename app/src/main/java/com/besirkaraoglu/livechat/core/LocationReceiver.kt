package com.besirkaraoglu.livechat.core

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.location.Location
import android.widget.Toast
import com.besirkaraoglu.livechat.core.service.LocationService
import com.besirkaraoglu.livechat.core.utils.LocationUtils

/**
 * Receiver for broadcasts sent by {@link LocationService}.
 */
class LocationReceiver : BroadcastReceiver() {

    override fun onReceive(p0: Context?, intent: Intent?) {
        val location = intent?.getParcelableExtra<Location>(LocationService.EXTRA_LOCATION)
        if (location != null) {
            Toast.makeText(p0, LocationUtils.getLocationText(location),
                Toast.LENGTH_SHORT).show();
        }
    }
}
