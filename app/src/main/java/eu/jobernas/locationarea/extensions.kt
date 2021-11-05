package eu.jobernas.locationarea

import android.app.ActivityManager
import android.content.Context
import android.content.Context.ACTIVITY_SERVICE

@SuppressWarnings("deprecation")
fun <T>Context.isMyServiceRunning(serviceClass: Class<T>): Boolean =
    (getSystemService(ACTIVITY_SERVICE) as ActivityManager)
        .getRunningServices(Integer.MAX_VALUE)
        .any { it.service.className == serviceClass.name }
