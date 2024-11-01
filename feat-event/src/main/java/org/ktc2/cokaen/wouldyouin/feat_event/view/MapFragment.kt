package org.ktc2.cokaen.wouldyouin.feat_event.view

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapView
import com.kakao.vectormap.RoadViewRequest
import com.kakao.vectormap.camera.CameraUpdateFactory
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.label.LabelStyle
import com.kakao.vectormap.label.LabelStyles
import org.ktc2.cokaen.wouldyouin.data.model.Location
import org.ktc2.cokaen.wouldyouin.feat_event.R
import org.ktc2.cokaen.wouldyouin.feat_event.databinding.FragmentMapBinding

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
