package com.tistory.s1s1s1.inu_contact

import android.graphics.Bitmap
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.ViewGroup
import android.widget.Button
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.tistory.s1s1s1.inu_contact.Network.GPS
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView


class MapActivity : AppCompatActivity() {

    private val mapView : MapView by lazy { MapView(this) }
    private val defaultMaker = ArrayList<MapPOIItem>()
    private var isDefaultMakerShowing = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)
        val btn = findViewById<Button>(R.id.btn_marker)
        // FIXME 마커 삭제후 다시 추가가 안됨.
        btn.setOnClickListener {
            val array = arrayOfNulls<MapPOIItem>(defaultMaker.size)
            if(isDefaultMakerShowing) {
                mapView.removePOIItems(defaultMaker.toArray(array))
                isDefaultMakerShowing = false
            } else {
                mapView.addPOIItems(defaultMaker.toArray(array))
                isDefaultMakerShowing = true
            }
        }
        val mapViewContainer = findViewById<ViewGroup>(R.id.map_view)
        mapViewContainer.addView(mapView)
        mapView.setMapViewEventListener(eventListener)


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
                            defaultMaker.add(marker)
//                            mapView.addPOIItem(marker)
                        }
                    })
        }
//        isDefaultMakerShowing = true

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
            mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithMarkerHeadingWithoutMapMoving)
            mapView.setShowCurrentLocationMarker(true)
            mapView.setDefaultCurrentLocationMarker()
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
