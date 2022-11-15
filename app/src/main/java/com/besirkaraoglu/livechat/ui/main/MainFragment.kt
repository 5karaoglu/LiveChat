package com.besirkaraoglu.livechat.ui.main

import android.Manifest
import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.content.SharedPreferences
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.net.Uri
import android.os.Bundle
import android.os.IBinder
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.viewModels
import com.besirkaraoglu.livechat.BuildConfig.APPLICATION_ID
import com.besirkaraoglu.livechat.R
import com.besirkaraoglu.livechat.core.LocationReceiver
import com.besirkaraoglu.livechat.core.base.BaseFragment
import com.besirkaraoglu.livechat.core.service.LocationService
import com.besirkaraoglu.livechat.core.utils.LocationServiceHandler
import com.besirkaraoglu.livechat.core.utils.LocationServiceHandlerImpl
import com.besirkaraoglu.livechat.core.utils.binding.viewBinding
import com.besirkaraoglu.livechat.core.utils.navigate
import com.besirkaraoglu.livechat.databinding.FragmentMainBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_INDEFINITE
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber


@AndroidEntryPoint
class MainFragment : BaseFragment(R.layout.fragment_main), OnMapReadyCallback,
    SharedPreferences.OnSharedPreferenceChangeListener,
    LocationServiceHandler by LocationServiceHandlerImpl() {
    private val TAG = "MainActivity"
    private val REQUEST_PERMISSIONS_REQUEST_CODE = 34

    private val binding by viewBinding(FragmentMainBinding::bind)
    private val viewModel by viewModels<MainViewModel>()

    private val MAPVIEW_BUNDLE_KEY = "MapViewBundleKey"
    private lateinit var mMapView: MapView

    private lateinit var map: GoogleMap
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setLocationHandler()

        initMapView(savedInstanceState)
        if (savedInstanceState == null) viewModel.onFragmentCreated()
        setUpView()
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())
    }

    private fun setLocationHandler() {
        setContext(requireActivity().applicationContext)
        /*setReceiver(LocationReceiver())*/
        registerLifecycleOwnerForLocationService(this)
    }

    private fun initMapView(savedInstanceState: Bundle?){
        mMapView = binding.mapView
        mMapView.getMapAsync(this)
        var mapViewBundle: Bundle? = null
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY)
        }
        mMapView.onCreate(mapViewBundle)
    }

    private fun setUpView() {
        binding.buttonSend.setOnClickListener { /*viewModel.sendData()*/
            navigate(R.id.action_mainFragment_to_userProfileBottomSheetFragment)
        }
        binding.buttonShareLocation.setOnClickListener { checkPermissionsAndRequestLocation() }
    }



    private fun checkPermissionsAndRequestLocation() {
        if (checkPermissions()){
            requestLocation()
        }else{
            locationPermissionRequest.launch(arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION))
        }
    }

    private fun requestLocation() {
        //Use service to get location
        bindService()
    }

    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                requestLocation()
            } else -> {
            // No location access granted.
        }
        }
    }

    private fun requestPermissions() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), ACCESS_COARSE_LOCATION)) {
            // Provide an additional rationale to the user. This would happen if the user denied the
            // request previously, but didn't check the "Don't ask again" checkbox.
            Timber.tag(TAG).i("Displaying permission rationale to provide additional context.")
            showSnackbar(R.string.permission_rationale, android.R.string.ok, View.OnClickListener {
                // Request permission
                startLocationPermissionRequest()
            })

        } else {
            // Request permission. It's possible this can be auto answered if device policy
            // sets the permission in a given state or the user denied the permission
            // previously and checked "Never ask again".
            Timber.tag(TAG).i("Requesting permission")
            startLocationPermissionRequest()
        }
    }


    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        Timber.tag(TAG).i("onRequestPermissionResult")
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            when {
                // If user interaction was interrupted, the permission request is cancelled and you
                // receive empty arrays.
                grantResults.isEmpty() -> Timber.tag(TAG).i("User interaction was cancelled.")

                // Permission granted.
                (grantResults[0] == PERMISSION_GRANTED) -> requestLocation()

                // Permission denied.

                // Notify the user via a SnackBar that they have rejected a core permission for the
                // app, which makes the Activity useless. In a real app, core permissions would
                // typically be best requested during a welcome-screen flow.

                // Additionally, it is important to remember that a permission might have been
                // rejected without asking the user for permission (device policy or "Never ask
                // again" prompts). Therefore, a user interface affordance is typically implemented
                // when permissions are denied. Otherwise, your app could appear unresponsive to
                // touches or interactions which have required permissions.
                else -> {
                    showSnackbar(R.string.permission_denied_explanation, R.string.settings,
                        View.OnClickListener {
                            // Build intent that displays the App settings screen.
                            val intent = Intent().apply {
                                action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                                data = Uri.fromParts("package", APPLICATION_ID, null)
                                flags = Intent.FLAG_ACTIVITY_NEW_TASK
                            }
                            startActivity(intent)
                        })
                }
            }
        }
    }

    private fun checkPermissions() =
        ActivityCompat.checkSelfPermission(requireContext(), ACCESS_COARSE_LOCATION) == PERMISSION_GRANTED

    private fun startLocationPermissionRequest() {
        ActivityCompat.requestPermissions(requireActivity(), arrayOf(ACCESS_COARSE_LOCATION),
            REQUEST_PERMISSIONS_REQUEST_CODE)
    }

    override fun onMapReady(map: GoogleMap) {
        this.map = map
        initLocationObserver()
        /*map.addMarker(MarkerOptions().position(LatLng(0.0, 0.0)).title("Marker"))*/
    }

    private fun initLocationObserver() {
        viewModel.location.observe(viewLifecycleOwner){
            map.addMarker(MarkerOptions().position(LatLng(it.latitude, it.longitude)).title("Marker"))
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = super.onCreateView(inflater, container, savedInstanceState)
        registerLifecycleOwner(this)
        return v
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        var mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY)
        if (mapViewBundle == null) {
            mapViewBundle = Bundle()
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle)
        }
        mMapView.onSaveInstanceState(mapViewBundle)
    }

    override fun onResume() {
        super.onResume()
        mMapView.onResume()
    }

    override fun onStart() {
        super.onStart()
        mMapView.onStart()
    }

    override fun onStop() {
        super.onStop()
        mMapView.onStop()
    }

    override fun onPause() {
        mMapView.onPause()
        super.onPause()
    }

    override fun onDestroyView() {
        mMapView.onDestroy()
        super.onDestroyView()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mMapView.onLowMemory()
    }

    override fun onSharedPreferenceChanged(p0: SharedPreferences?, p1: String?) {

    }

    /**
     * Shows a [Snackbar].
     *
     * @param snackStrId The id for the string resource for the Snackbar text.
     * @param actionStrId The text of the action item.
     * @param listener The listener associated with the Snackbar action.
     */
    private fun showSnackbar(
        snackStrId: Int,
        actionStrId: Int = 0,
        listener: View.OnClickListener? = null
    ) {
        val snackbar = Snackbar.make(requireActivity().findViewById(android.R.id.content), getString(snackStrId),
            LENGTH_INDEFINITE)
        if (actionStrId != 0 && listener != null) {
            snackbar.setAction(getString(actionStrId), listener)
        }
        snackbar.show()
    }
}