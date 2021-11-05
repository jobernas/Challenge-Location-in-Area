package eu.jobernas.locationarea.location

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.PolyUtil
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class Utils private constructor() {

    /**
     * Static Methods
     */
    companion object {

        // Constants
        const val TAG = "UTILS"
        const val EARTH_R = 6371e3

        // Check if point is contained by polygon
        fun isPointInQuadrant(location: LatLng, area: PolygonArea): Boolean {
            val points = area.points
            return PolyUtil.containsLocation(location, points, false)
        }

        // Find Quadrant for position
        fun findAreaForPosition(areas: List<PolygonArea>, location: LatLng): PolygonArea? {
            return areas.find { isPointInQuadrant(location, it) }
        }

        fun getDistanceBetweenLocationAB(locationA: LatLng, locationB: LatLng): Double {
            val a1 = locationA.latitude * Math.PI/180
            val a2 = locationB.latitude * Math.PI/180
            val b1 = (locationB.latitude-locationA.latitude) * Math.PI/180
            val b2 = (locationB.longitude-locationA.longitude) * Math.PI/180
            val a = sin(b1/2) * sin(b1/2) +
                    cos(a1) * cos(a2) *
                    sin(b2/2) * sin(b2/2)
            val c = 2 * atan2(sqrt(a), sqrt(1-a))
            return EARTH_R * c // in metres
        }

    }
}