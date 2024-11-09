package org.ktc2.cokaen.wouldyouin.feat_event.view

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.Priority
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapView
import com.kakao.vectormap.camera.CameraUpdateFactory
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.label.LabelStyle
import com.kakao.vectormap.label.LabelStyles
import org.ktc2.cokaen.wouldyouin.data.model.Location
import org.ktc2.cokaen.wouldyouin.feat_event.R
import org.ktc2.cokaen.wouldyouin.feat_event.databinding.FragmentMapBinding
import android.Manifest
import android.content.Intent
import android.widget.Toast
import com.kakao.vectormap.label.Label

class MapFragment : Fragment() {

    private lateinit var binding: FragmentMapBinding
    private lateinit var mapView: MapView
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private var kakaoMap: KakaoMap? = null
    private var mapInitialized = false
    private var centerLabel: Label? = null

    /*
    private val locations = listOf(
        Location(name = "행사 장소 1", latitude = 35.1784, longitude = 126.9096),
        Location(name = "행사 장소 2", latitude = 35.1790, longitude = 126.9096),
        Location(name = "행사 장소 3", latitude = 35.1784, longitude = 126.9100)
    )*/

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_map, container, false)
        mapView = binding.mapView


        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        // 위치 요청 생성, 2초 간격 업데이트
        locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 2000L).apply {
            setMinUpdateIntervalMillis(1000L)
        }.build()


        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                for (location in locationResult.locations) {
                    updateMapWithCurrentLocation(location.latitude, location.longitude)
                }
            }
        }


        if (checkLocationPermission()) {
            getCurrentLocationAndStartMap()
        } else {
            requestLocationPermission()
        }


        mapView.start(object : MapLifeCycleCallback() {
            override fun onMapDestroy() {}
            override fun onMapError(error: Exception?) {}
        }, object : KakaoMapReadyCallback() {
            override fun onMapReady(kakaoMap: KakaoMap) {
                this@MapFragment.kakaoMap = kakaoMap
                if (!mapInitialized) {
                    mapInitialized = true
                    getCurrentLocationAndStartMap()
                    //addMarkersToMap()
                }
            }
        })

        mapView.visibility = View.INVISIBLE

        binding.cardView.setOnClickListener {
            val intent = Intent(requireContext(), EventDetailActivity::class.java)
            //행사 Id 또는 정보 전달 시
            //intent.putExtra("place_name", "새벽 울림")
            startActivity(intent)
        }

        return binding.root
    }

    private fun checkLocationPermission(): Boolean {
        val fineLocation = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
        val coarseLocation = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
        return fineLocation == PackageManager.PERMISSION_GRANTED || coarseLocation == PackageManager.PERMISSION_GRANTED
    }

    private fun requestLocationPermission() {
        requestPermissions(
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
            LOCATION_PERMISSION_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getCurrentLocationAndStartMap()
            } else {
                mapView.visibility = View.VISIBLE
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun getCurrentLocationAndStartMap() {
        Toast.makeText(requireContext(), "위치 정보를 불러오고 있습니다", Toast.LENGTH_SHORT).show()

        fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
            .addOnSuccessListener { location ->
                if (location != null) {
                    val currentLatLng = LatLng.from(location.latitude, location.longitude)
                    updateMapWithCurrentLocation(location.latitude, location.longitude)
                    addCenterLabel(currentLatLng)

                    mapView.visibility = View.VISIBLE
                }
            }
    }

    private fun addCenterLabel(currentLatLng: LatLng) {
        kakaoMap?.labelManager?.layer?.let { labelLayer ->
            val markerBitmap = BitmapFactory.decodeResource(resources, R.drawable.my_location)
            val scaledBitmap = Bitmap.createScaledBitmap(markerBitmap, 25, 25, true)
            val labelStyle = LabelStyle.from(scaledBitmap).setAnchorPoint(0.5f, 0.5f)

            centerLabel = labelLayer.addLabel(
                LabelOptions.from("centerLabel", currentLatLng)
                    .setStyles(labelStyle)
            )

            // TrackingManager 사용 -> Label을 내 위치로 추적 설정
            kakaoMap?.trackingManager?.startTracking(centerLabel)
        }
    }

    /*
    private fun addMarkersToMap() {
        kakaoMap?.let { map ->
            val lodLabelLayer = map.labelManager?.lodLayer
            locations.forEach { location ->
                val markerBitmap = BitmapFactory.decodeResource(resources, R.drawable.marker)
                val scaledBitmap = Bitmap.createScaledBitmap(markerBitmap, 50, 50, true)
                val labelStyle = LabelStyle.from(scaledBitmap)
                val labelStyles = LabelStyles.from(labelStyle)
                val options = LabelOptions.from(LatLng.from(location.latitude, location.longitude))
                    .setStyles(labelStyles)
                lodLabelLayer?.addLodLabel(options)
            }
        }
    }*/

    private fun updateMapWithCurrentLocation(latitude: Double, longitude: Double) {
        kakaoMap?.moveCamera(CameraUpdateFactory.newCenterPosition(LatLng.from(latitude, longitude), 15))
        centerLabel?.moveTo(LatLng.from(latitude, longitude))
    }

    override fun onResume() {
        super.onResume()
        mapView.resume()
        if (checkLocationPermission()) {
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
        }
    }

    override fun onPause() {
        super.onPause()
        mapView.pause()
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }
}

//기존 코드(혹시 몰라서 남겨 놓음)
/*
class MapFragment : Fragment() {

    private lateinit var binding: FragmentMapBinding
    private lateinit var mapView: MapView
    private val locations = listOf(
        Location(name = "행사 장소 1", latitude = 37.5665, longitude = 126.9780),
        Location(name = "행사 장소 2", latitude = 37.5655, longitude = 126.9770),
        Location(name = "행사 장소 3", latitude = 37.5645, longitude = 126.9760)
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_map, container, false)
        binding.map = this

        mapView = binding.mapView
        mapView.start(object : MapLifeCycleCallback() {
            override fun onMapDestroy() {
                // 지도 API가 정상적으로 종료될 때 호출됨
            }

            override fun onMapError(error: Exception?) {
                // 인증 실패 및 지도 사용 중 에러가 발생할 때 호출됨
            }
        }, object : KakaoMapReadyCallback() {
            override fun onMapReady(kakaoMap: KakaoMap) {
                // 인증 후 API가 정상적으로 실행될 때 호출됨

                val lodLabelLayer = kakaoMap.labelManager?.lodLayer

                locations.forEach { location ->
                    val markerBitmap = BitmapFactory.decodeResource(resources, R.drawable.marker)
                    val scaledBitmap = Bitmap.createScaledBitmap(markerBitmap, 50, 50, true) // 50x50으로 조정

                    val labelStyle = LabelStyle.from(scaledBitmap)
                    val labelStyles = LabelStyles.from(labelStyle)

                    val options = LabelOptions.from(LatLng.from(location.latitude, location.longitude))
                        .setStyles(labelStyles)

                    lodLabelLayer?.addLodLabel(options)
                }

                if (locations.isNotEmpty()) {
                    val firstLocation = locations[0]
                    kakaoMap.moveCamera(CameraUpdateFactory.newCenterPosition(LatLng.from(firstLocation.latitude, firstLocation.longitude), 15))
                }


            }
        })

        binding.cardView.setOnClickListener {
            val intent = Intent(requireContext(), EventDetailActivity::class.java)
            //행사 Id 또는 정보 전달 시
            //intent.putExtra("place_name", "새벽 울림")
            startActivity(intent)
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        mapView.resume()
    }

    override fun onPause() {
        super.onPause()
        mapView.pause()
    }
}
*/