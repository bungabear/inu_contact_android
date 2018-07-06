package com.tistory.s1s1s1.inu_contact

import android.graphics.Bitmap
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.tistory.s1s1s1.inu_contact.Network.GPS
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView


class MapActivity : AppCompatActivity() {

    private val mapView : MapView by lazy { MapView(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        val mapViewContainer = findViewById<View>(R.id.map_view) as ViewGroup
        mapViewContainer.addView(mapView)
        mapView.setMapViewEventListener(eventListener)

        mapView.setShowCurrentLocationMarker(true)
        mapView.setZoomLevel(9, false)


        Singleton.markerJson.forEachIndexed { it, json ->
            val num = if(it < 9) "0${it+1}" else "${it+1}"
            Glide.with (this@MapActivity)
                    .asBitmap()
                    .load("https://sc.inu.ac.kr/inumportal/jsp/inu/common/images/info/map/pin$num.png")
                    .into( object : SimpleTarget<Bitmap>(){
                        override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                            val marker = MapPOIItem()
                            marker.tag = (it+1)
                            marker.itemName = json.asJsonObject.get("title").asString
                            marker.markerType = MapPOIItem.MarkerType.CustomImage // 기본으로 제공하는 BluePin 마커 모양.
                            marker.customImageBitmap = resource
                            marker.mapPoint = MapPoint.mapPointWithGeoCoord(json.asJsonObject.get("lat").asDouble, json.asJsonObject.get("log").asDouble)
                            mapView.addPOIItem(marker)
                        }
                    })
        }

        // 학교 중앙 좌표 37.37545394897461 126.63258361816406
        getMyLocation()!!.run {
            mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(37.37545394897461, 126.63258361816406), true)
        }
    }

    // 일회성 위치 조회.
    private fun getMyLocation():Pair<Double, Double>? {
        val gps = GPS(this)
        // GPS 사용유무 가져오기
        return if (gps.isGetLocation) {
            val latitude = gps.latitude
            val longitude = gps.longitude
           Pair(latitude, longitude)
        } else {
            // GPS 를 사용할수 없으므로
            null
        }
    }

    private val eventListener = object : MapView.MapViewEventListener {
        override fun onMapViewDoubleTapped(p0: MapView?, p1: MapPoint?) {
        }

        override fun onMapViewInitialized(p0: MapView?) {
            mapView.setZoomLevel(2, true)
        }

        override fun onMapViewDragStarted(p0: MapView?, p1: MapPoint?) {
        }

        override fun onMapViewMoveFinished(p0: MapView?, p1: MapPoint?) {
        }

        override fun onMapViewCenterPointMoved(p0: MapView?, p1: MapPoint?) {
//            Log.d(TAG, "${p1!!.mapPointGeoCoord.latitude} ${p1!!.mapPointGeoCoord.longitude}" )
        }

        override fun onMapViewDragEnded(p0: MapView?, p1: MapPoint?) {
        }

        override fun onMapViewSingleTapped(p0: MapView?, p1: MapPoint?) {
        }

        override fun onMapViewZoomLevelChanged(p0: MapView?, p1: Int) {
            Log.d(TAG, "$p1")
        }

        override fun onMapViewLongPressed(p0: MapView?, p1: MapPoint?) {
        }
    }
}
