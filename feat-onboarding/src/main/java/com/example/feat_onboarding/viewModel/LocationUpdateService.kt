package com.example.feat_onboarding.viewModel

import android.Manifest
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import kotlin.math.pow

class LocationUpdateService : Service() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback

    override fun onCreate() {
        super.onCreate()

        Log.d("LocationUpdateService", "Service started") // 서비스 시작 로그

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // 위치 업데이트 콜백 설정
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                for (location in locationResult.locations) {
                    updateLocationInPreferences(location)
                }
            }
        }

        startLocationUpdates()
    }

    private fun startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val locationRequest = LocationRequest.create().apply {
                interval = 20000 // 위치 업데이트 간격
                fastestInterval = 15000
                priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            }

            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
        }
    }

    private fun updateLocationInPreferences(location: Location) {
        val sharedPreferences = getSharedPreferences("LocationData", Context.MODE_PRIVATE)

        // 화면의 좌표 계산
        val visibleRegion = calculateVisibleRegion(location.latitude, location.longitude, 15)
        val topLeft = visibleRegion.first
        val bottomRight = visibleRegion.second

        with(sharedPreferences.edit()) {
            putString("centerLat", location.latitude.toString())
            putString("centerLng", location.longitude.toString())
            putString("topLeftLat", topLeft.first.toString())
            putString("topLeftLng", topLeft.second.toString())
            putString("bottomRightLat", bottomRight.first.toString())
            putString("bottomRightLng", bottomRight.second.toString())
            commit() // 데이터 즉시 반영
        }
        Log.d("LocationUpdateService", "Updated location in SharedPreferences: ${location.latitude}, ${location.longitude}")
        Log.d("LocationUpdateService", "Top Left: ${topLeft.first}, ${topLeft.second}, Bottom Right: ${bottomRight.first}, ${bottomRight.second}")
    }

    override fun onDestroy() {
        super.onDestroy()
        // 서비스가 종료될 때 위치 업데이트 중지
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun calculateVisibleRegion(centerLat: Double, centerLng: Double, zoomLevel: Int): Pair<Pair<Double, Double>, Pair<Double, Double>> {
        val scale = 2.0.pow(zoomLevel.toDouble())
        val halfMapWidthInDegrees = (180 / scale)

        val topLeftLat = centerLat + halfMapWidthInDegrees
        val topLeftLng = centerLng - halfMapWidthInDegrees
        val bottomRightLat = centerLat - halfMapWidthInDegrees
        val bottomRightLng = centerLng + halfMapWidthInDegrees

        return Pair(Pair(topLeftLat, topLeftLng), Pair(bottomRightLat, bottomRightLng))
    }

}
