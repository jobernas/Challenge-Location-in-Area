package eu.jobernas.locationarea.location

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.google.android.gms.maps.model.LatLng
import eu.jobernas.locationarea.BuildConfig
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class LocationService: Service() {

    companion object {
        private const val TAG = "LOCATION_SERV"
    }

    /**
     * Private Vars
     */
    private var compositeDisposable: CompositeDisposable? = null
    private var userLocationManager: UserLocationManager? = null

    /**
     * Super Methods
     **/

    override fun onBind(p0: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate")
        userLocationManager = UserLocationManager(this)
        compositeDisposable = CompositeDisposable()
        addUserLocationObservable()
    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy")
        compositeDisposable?.dispose()
        compositeDisposable = null
        super.onDestroy()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    /***
     * Private Methods
     */
    private fun addUserLocationObservable() {
        val disposable = UserLocationManager
            .lastLocation
            .subscribeOn(Schedulers.io())
            .throttleLast(30, TimeUnit.SECONDS) // 1st Filter, Time Filter
            .observeOn(Schedulers.computation())
            .subscribe { userLocation ->
                Log.d(TAG, "Location: ${userLocation.latitude}, ${userLocation.longitude}")
                val location = LatLng(userLocation.latitude, userLocation.longitude)

                // 2st Filter - Check if location is inside last location known
                if (Utils.isPointInQuadrant())

            }
        compositeDisposable?.add(disposable)
    }

    private fun


}