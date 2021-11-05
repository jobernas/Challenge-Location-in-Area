package eu.jobernas.locationarea.location

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class LocationService: Service() {

    companion object {
        private const val TAG = "LOCATION_SERV"
    }

    /**
     * Private Vars
     */
    private var compositeDisposable: CompositeDisposable? = null

    /**
     * Super Methods
     **/

    override fun onBind(p0: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate")
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
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {

            }
        compositeDisposable?.add(disposable)
    }


}