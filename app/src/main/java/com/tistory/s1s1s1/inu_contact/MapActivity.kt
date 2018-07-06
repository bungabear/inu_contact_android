package com.tistory.s1s1s1.inu_contact

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.ViewGroup
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView
import java.util.ArrayList
import android.widget.Toast
import com.tistory.s1s1s1.inu_contact.Network.GPS


class MapActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)
        val mapView = MapView(this)
        val mapViewContainer = findViewById<View>(R.id.map_view) as ViewGroup
        mapViewContainer.addView(mapView)
        getMyLocation()!!.run {
            mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(this.first, this.second), true);
        }
    }

    // 일회성 위치 조회.
    fun getMyLocation():Pair<Double, Double>? {
        val gps = GPS(this)
        // GPS 사용유무 가져오기
        if (gps.isGetLocation()) {
            val latitude = gps.getLatitude()
            val longitude = gps.getLongitude()
            return Pair(latitude, longitude)
        } else {
            // GPS 를 사용할수 없으므로
            return null
        }
    }
}
