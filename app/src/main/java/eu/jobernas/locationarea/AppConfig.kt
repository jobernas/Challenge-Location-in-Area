package eu.jobernas.locationarea

object AppConfig {

    object GPS {
        const val interval: Long = 15000 // Requests Interval Millis 15 secs
        const val fastestInterval: Long = 5000 // Minimum 5 secs
        const val smallestDisplacement: Float = 1f // 1m
    }
}