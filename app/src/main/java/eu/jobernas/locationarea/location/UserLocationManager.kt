package eu.jobernas.locationarea.location

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import eu.jobernas.locationarea.AppConfig
import io.reactivex.rxjava3.subjects.PublishSubject

class UserLocationManager(private var activity: Activity) {

    companion object {
        private const val TAG = "LOCATION_MANAGER"
        val lastKnownLocation: PublishSubject<Location> = PublishSubject.create()
        val lastLocation: PublishSubject<Location> = PublishSubject.create()
        val onLocationAvailabilityChanged: PublishSubject<LocationAvailability> = PublishSubject.create()
    }

    private var isSearching: Boolean = false
    private var fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(activity)

    private var locationRequest: LocationRequest =
        LocationRequest.create().apply {
            interval = AppConfig.GPS.interval
            fastestInterval = AppConfig.GPS.fastestInterval
            smallestDisplacement = AppConfig.GPS.smallestDisplacement
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY // High accuracy for location
        }

    private var locationCallback: LocationCallback = object : LocationCallback() {

        override fun onLocationResult(p0: LocationResult?) {
            val locationResult = p0.takeIf { it != null } ?: return

            if (locationResult.locations.isNotEmpty()) {
                // get latest location
                Log.d(TAG, "Last Location Latitude: ${locationResult.lastLocation.latitude}")
                Log.d(TAG, "Last Location Longitude: ${locationResult.lastLocation.longitude}")
                Log.d(TAG, "Last Location Altitude: ${locationResult.lastLocation.altitude}")
                lastLocation.onNext(locationResult.lastLocation)
            }
        }

        override fun onLocationAvailability(p0: LocationAvailability?) {
            val locationAvailability = p0.takeIf { it != null } ?: return
            onLocationAvailabilityChanged.onNext(locationAvailability)
            Log.d(TAG, "isLocation Available: ${locationAvailability.isLocationAvailable}")
        }
    }

    /**
     * Public Methods
     */
    //start location updates
    @SuppressLint("MissingPermission")
    fun startLocationUpdates() {
        isSearching = true
        if (checkPermissions())
            return
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            null /* Looper */
        )
    }

    // stop location updates
    fun stopLocationUpdates() {
        isSearching = false
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    // Get if is Searching for Position
    fun isSearchingPosition(): Boolean = isSearching

    // Get Last Known Location
    @SuppressLint("MissingPermission")
    fun getLastKnownLocation() {
        if (checkPermissions())
            return

        fusedLocationClient
            .lastLocation
            .addOnSuccessListener { location ->
                if (location != null) {
                    Log.d(TAG, "Last Known Latitude: ${location.latitude}")
                    Log.d(TAG, "Last Known Longitude: ${location.longitude}")
                    Log.d(TAG, "Last Known Altitude: ${location.altitude}")
                    lastKnownLocation.onNext(location)
                    // use your location object
                    // get latitude , longitude and other info from this
                } else {
                    Log.d(TAG, "Last Known Location is Not Available.")
                }
            }
    }

    // Check if GPS is Enable
    fun isLocationEnabled(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            // This is new method provided in API 28
            val lm = activity.getSystemService(Context.LOCATION_SERVICE) as? LocationManager
            lm?.isLocationEnabled ?: false
        } else {
            // This is Deprecated in API 28
            val mode: Int = Settings.Secure.getInt(
                activity.contentResolver, Settings.Secure.LOCATION_MODE,
                Settings.Secure.LOCATION_MODE_OFF
            )
            mode != Settings.Secure.LOCATION_MODE_OFF
        }
    }

    // Check Location Permissions
    private fun checkPermissions(): Boolean = ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED

}