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
import android.util.Log
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.kakao.vectormap.label.Label
import com.kakao.vectormap.label.LodLabel
import com.kakao.vectormap.label.LodLabelLayer
import org.ktc2.cokaen.wouldyouin.core.DateTimeUtils
import org.ktc2.cokaen.wouldyouin.data.model.EventResponse
import kotlin.math.pow
import kotlin.math.sqrt

class MapFragment : Fragment() {

    private lateinit var binding: FragmentMapBinding
    private lateinit var mapView: MapView
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private var kakaoMap: KakaoMap? = null
    private var mapInitialized = false
    private var centerLabel: Label? = null
    private lateinit var eventList: Array<EventResponse> // 전달된 이벤트 목록을 저장
    private var selectedEvent: EventResponse? = null // 현재 선택된 이벤트 정보

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

    //데이터 받기
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.getStringArray("eventList")?.let { jsonEventList ->
            val gson = Gson()
            eventList = jsonEventList.map { gson.fromJson(it, EventResponse::class.java) }.toTypedArray()
        } ?: run {
            eventList = emptyArray()
        }
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
                    addMarkersToMap()
                }
            }
        })

        mapView.visibility = View.INVISIBLE

        binding.cardView.setOnClickListener {
            selectedEvent?.let { event ->
                val intent = Intent(requireContext(), EventDetailActivity::class.java)
                intent.putExtra("event_id", event.id)
                startActivity(intent)
            }
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

    // **[추가: 거리 계산 함수]**
    private fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val earthRadius = 6371.0 // km
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = Math.sin(dLat / 2).pow(2.0) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                Math.sin(dLon / 2).pow(2.0)
        val c = 2 * Math.atan2(sqrt(a), sqrt(1 - a))
        return earthRadius * c
    }

    // **[추가: 가장 가까운 이벤트 찾기 함수]**
    private fun findClosestEvent(currentLatitude: Double, currentLongitude: Double): EventResponse? {
        return eventList.minByOrNull { event ->
            calculateDistance(
                currentLatitude, currentLongitude,
                event.location.latitude, event.location.longitude
            )
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

                    // **[수정: 가장 가까운 이벤트로 카드뷰 초기화]**
                    val closestEvent = findClosestEvent(location.latitude, location.longitude)
                    closestEvent?.let { updateCardViewWithEvent(it) }

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

    private fun addMarkersToMap() { //이벤트 마커를 추가하는 함수 구현
        kakaoMap?.let { map ->
            val lodLabelLayer = map.labelManager?.lodLayer
            eventList.forEach { event ->
                val markerBitmap = BitmapFactory.decodeResource(resources, R.drawable.marker)
                val scaledBitmap = Bitmap.createScaledBitmap(markerBitmap, 50, 50, true)
                val labelStyle = LabelStyle.from(scaledBitmap)
                val labelStyles = LabelStyles.from(labelStyle)
                val options = LabelOptions.from(LatLng.from(event.location.latitude, event.location.longitude))
                    .setStyles(labelStyles)

                /*
                //라벨 클릭 시 선택한 이벤트 정보를 업데이트
                val label = lodLabelLayer?.addLodLabel(options)
                label?.let {
                    it.setOnClickListener {
                        updateCardViewWithEvent(event) // 라벨 클릭 시 카드뷰 업데이트
                    }
                }
            }*/
                // 라벨 생성 및 이벤트 정보 설정
                val label = lodLabelLayer?.addLodLabel(options)
                label?.tag = event // 라벨에 해당 이벤트 데이터 설정
            }

            // 라벨 클릭 리스너 설정
            map.setOnLodLabelClickListener(object : KakaoMap.OnLodLabelClickListener {
                override fun onLodLabelClicked(
                    kakaoMap: KakaoMap?,
                    layer: LodLabelLayer?,
                    label: LodLabel?
                ) {
                    // 클릭한 라벨에서 이벤트 데이터 가져오기
                    val event = label?.tag as? EventResponse
                    event?.let { selectedEvent ->
                        // 라벨 클릭 시 카드뷰 업데이트
                        updateCardViewWithEvent(selectedEvent)
                    }
                }
            })
        }
    }

    private fun updateMapWithCurrentLocation(latitude: Double, longitude: Double) {
        kakaoMap?.moveCamera(CameraUpdateFactory.newCenterPosition(LatLng.from(latitude, longitude), 15))
        centerLabel?.moveTo(LatLng.from(latitude, longitude))
        // 현재 위도, 경도 로그 출력
        Log.d("MapFragment", "Current Location - Latitude: $latitude, Longitude: $longitude")
    }

    //선택한 이벤트 정보로 카드뷰의 내용을 업데이트하는 함수
    private fun updateCardViewWithEvent(event: EventResponse) {
        selectedEvent = event //현재 선택된 이벤트 업데이트
        binding.placeName.text = event.title
        //binding.placeDescription.text = event.content
        //해쉬태그(안되면 생략..)
        binding.placeTags.text = event.host.hashtags.joinToString(" ")
        binding.placeAddress.text = event.location.detailAddress
        binding.placeDatetime.text = event.startTime.let(DateTimeUtils::formatDateTimeString)
        binding.imageUrl = event.host.profileImageUrl
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
