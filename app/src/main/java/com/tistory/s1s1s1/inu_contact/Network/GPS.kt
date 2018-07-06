package com.tistory.s1s1s1.inu_contact.Network

import android.annotation.TargetApi
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.support.v4.content.ContextCompat

class GPS(private val mContext: Context) : Service(), LocationListener {

    // 현재 GPS 사용유무
    private var isGPSEnabled = false
    // 네트워크 사용유무
    private var isNetworkEnabled = false
    // GPS 상태값
    var isGetLocation = false
        internal set
    private var location: Location? = null
    private var lat: Double = .0 // 위도
    private var lon: Double = .0 // 경도
    private var locationManager: LocationManager? = null

    val latitude: Double
        get() = if(location != null) location!!.latitude else lat

    val longitude: Double
        get() = if(location != null) location!!.longitude else lon

    @TargetApi(23)
    fun getLocation(): Location? {
        if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(
                        mContext, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(
                        mContext, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return null
        }

        try {
            locationManager = mContext
                    .getSystemService(Context.LOCATION_SERVICE) as LocationManager

            // GPS 정보 가져오기
            isGPSEnabled = locationManager!!
                    .isProviderEnabled(LocationManager.GPS_PROVIDER)

            // 현재 네트워크 상태 값 알아오기
            isNetworkEnabled = locationManager!!
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER)

            if (!isGPSEnabled && !isNetworkEnabled) {
                // GPS 와 네트워크사용이 가능하지 않을때 소스 구현
            } else {
                this.isGetLocation = true
                // 네트워크 정보로 부터 위치값 가져오기
                if (isNetworkEnabled) {
                    locationManager!!.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES.toFloat(), this)
                    if (locationManager != null) {
                        location = locationManager!!
                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                        if (location != null) {
                            // 위도 경도 저장
                            lat = location!!.latitude
                            lon = location!!.longitude
                        }
                    }
                }
                if (isGPSEnabled) {
                    if (location == null) {
                        locationManager!!.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES.toFloat(), this)
                        if (locationManager != null) {
                            location = locationManager!!
                                    .getLastKnownLocation(LocationManager.GPS_PROVIDER)
                            if (location != null) {
                                lat = location!!.latitude
                                lon = location!!.longitude
                            }
                        }
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return location
    }

    fun stopUsingGPS() {
        if (locationManager != null) {
            locationManager!!.removeUpdates(this@GPS)
        }
    }

    override fun onBind(arg0: Intent): IBinder? {
        return null
    }

    override fun onLocationChanged(location: Location) {
    }

    override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {
    }

    override fun onProviderEnabled(provider: String) {
    }

    override fun onProviderDisabled(provider: String) {
    }

    companion object {
        // 최소 GPS 정보 업데이트 거리 10미터
        private val MIN_DISTANCE_CHANGE_FOR_UPDATES: Long = 10

        // 최소 GPS 정보 업데이트 시간 밀리세컨이므로 1분
        private val MIN_TIME_BW_UPDATES = (1000 * 60 * 1).toLong()
    }
}

