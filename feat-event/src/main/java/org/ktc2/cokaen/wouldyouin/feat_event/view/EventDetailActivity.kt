package org.ktc2.cokaen.wouldyouin.feat_event.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapView
import dagger.hilt.android.AndroidEntryPoint
import org.ktc2.cokaen.wouldyouin.core.DateTimeUtils
import org.ktc2.cokaen.wouldyouin.core_navigation.ActivityNavigationOptions
import org.ktc2.cokaen.wouldyouin.core_navigation.DeepLinkDestinations
import org.ktc2.cokaen.wouldyouin.core_navigation.NavigationCommand
import org.ktc2.cokaen.wouldyouin.core_navigation.NavigationDestination
import org.ktc2.cokaen.wouldyouin.core_navigation.NavigationUtil
import org.ktc2.cokaen.wouldyouin.feat_booking.view.BookingActivity
import org.ktc2.cokaen.wouldyouin.feat_event.R
import org.ktc2.cokaen.wouldyouin.feat_event.databinding.ActivityEventDetailBinding
import org.ktc2.cokaen.wouldyouin.feat_event.view.viewmodel.EventViewModel
import javax.inject.Inject

@AndroidEntryPoint
class EventDetailActivity : AppCompatActivity() {
    @Inject
    lateinit var navigationUtil: NavigationUtil
    private lateinit var binding: ActivityEventDetailBinding
    private lateinit var mapView: MapView
    private val eventViewModel: EventViewModel by viewModels()
    //행사 위치 변수 추가
    private var eventLatitude: Double? = null
    private var eventLongitude: Double? = null
    private var kakaoMap: KakaoMap? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_event_detail)
        //binding.eventdetail = this

        val eventId = intent.getLongExtra("event_id", -1)
        if (eventId != -1L) {
            eventViewModel.fetchEventDetails(eventId, this)
        }

        eventViewModel.eventDetails.observe(this) { eventResponse ->
            eventResponse?.let { event ->
                binding.eventName.text = event.data?.title
                binding.eventTime.text = event.data?.startTime?.let(DateTimeUtils::formatDateTimeString) ?: "시간 정보 없음"
                binding.eventEndTime.text = event.data?.endTime?.let(DateTimeUtils::formatDateTimeString) ?: "시간 정보 없음"
                //binding.eventDuration.text = "약 ${DateTimeUtils.formatDateTimeString(event.data!!.endTime)} - ${DateTimeUtils.formatDateTimeString(event.data!!.startTime)}분"
                binding.eventLocation.text = event.data?.location?.detailAddress
                binding.eventFee.text = "₩${event.data?.price}"
                binding.eventSeats.text = "${event.data?.leftSeat}/${event.data?.totalSeat}"
                binding.eventDescription.text = event.data?.content

                //행사 위치 정보 설정
                eventLatitude = event.data?.location?.latitude
                eventLongitude = event.data?.location?.longitude

                //추가
                // 지도 업데이트
                if (eventLatitude != null && eventLongitude != null && mapView.visibility == View.VISIBLE) {
                    kakaoMap?.moveCamera(
                        com.kakao.vectormap.camera.CameraUpdateFactory.newCenterPosition(
                            LatLng.from(eventLatitude!!, eventLongitude!!), 15
                        )
                    )
                }

                //주최자 정보 설정
                binding.organizerName.text = event.data?.host?.nickname
                binding.organizerInfo.text = event.data?.host?.intro
                binding.eventDescription.text = event.data?.content
                binding.contactPhone.text = event.data?.host?.phone
                binding.contactEmail.text = event.data?.host?.email

                binding.posterImageUrl = event.data?.images?.firstOrNull()
                binding.organizerImageUrl = event.data?.host?.profileImageUrl
            }
        }

        binding.bookButton.setOnClickListener {
            val eventId = eventViewModel.eventDetails.value?.data?.id
            if(eventId != null) {
                startBookingActivity(eventId)
            }
        }

        binding.viewProfile.setOnClickListener {
            val hostId = eventViewModel.eventDetails.value?.data?.host?.hostId
            if (hostId != null) {
                startMemberProfileActivity(hostId)
            }
        }

        mapView = binding.mapView
        mapView.start(object : MapLifeCycleCallback() {
            override fun onMapDestroy() {
                // 지도 API가 정상적으로 종료될 때 호출됨
                Log.d("MapView", "Map is end")
            }

            override fun onMapError(error: Exception?) {
                // 인증 실패 및 지도 사용 중 에러가 발생할 때 호출됨
                Log.e("MapView", "Map initialization error", error)
            }
        }, object : KakaoMapReadyCallback() {
            override fun onMapReady(kakaoMap: KakaoMap) {
                // 인증 후 API가 정상적으로 실행될 때 호출됨
                Log.d("MapView", "Map is ready")

                //추가
                this@EventDetailActivity.kakaoMap = kakaoMap

                // 지도 초기화 후 위치 설정
                if (eventLatitude != null && eventLongitude != null) {
                    Log.d("MapView", "Event Latitude: $eventLatitude, Event Longitude: $eventLongitude")
                    kakaoMap.moveCamera(
                        com.kakao.vectormap.camera.CameraUpdateFactory.newCenterPosition(
                            LatLng.from(eventLatitude!!, eventLongitude!!), 15
                        )
                    )
                } else {
                    Log.e("MapView", "Event location is not available yet")
                }
                /*
                //행사 위치로 카메라 이동
                eventLatitude?.let { latitude ->
                    eventLongitude?.let { longitude ->
                        Log.d("MapView", "Event Latitude: $latitude, Event Longitude: $longitude")
                        kakaoMap.moveCamera(
                            com.kakao.vectormap.camera.CameraUpdateFactory.newCenterPosition(
                                LatLng.from(latitude, longitude), 15
                            )
                        )
                    } ?: Log.e("MapView", "Longitude is null")
                } ?: Log.e("MapView", "Latitude is null") */
            }
        })
    }

    override fun onResume() {
        super.onResume()
        mapView.resume()

        //추가
        // 지도 상태 갱신
        if (eventLatitude != null && eventLongitude != null && kakaoMap != null) {
            kakaoMap?.moveCamera(
                com.kakao.vectormap.camera.CameraUpdateFactory.newCenterPosition(
                    LatLng.from(eventLatitude!!, eventLongitude!!), 15
                )
            )
        }
    }

    override fun onPause() {
        super.onPause()
        mapView.pause()
    }

    private fun startBookingActivity(eventId: Long) {
        Log.d("EventDetailActivity", "Navigating to BookingActivity with eventId: $eventId")
        /*
        navigationUtil.navigate(
            NavigationCommand(
                destination = NavigationDestination.Activity(DeepLinkDestinations.PAYMENT_CHECK_ACTIVITY),
                activityOptions = ActivityNavigationOptions(
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK,
                    clearTop = true
                ),
                data = mapOf("eventId" to eventId.toString()) // 여기에 필요한 데이터(id) 넘겨주시면 됩니다!!
            )
        )*/
        val intent = Intent(this, BookingActivity::class.java).apply {
            putExtra("eventId", eventId.toString()) // 데이터 추가
        }
        startActivity(intent)
    }

    private fun startMemberProfileActivity(hostId: Long) {
        Log.d("EventDetailActivity", "Navigating to BookingActivity with hostId: $hostId")
        /*
        navigationUtil.navigate(
            NavigationCommand(
                destination = NavigationDestination.Activity(DeepLinkDestinations.HOST_PROFILE_ACTIVITY),
                activityOptions = ActivityNavigationOptions(
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK,
                    clearTop = true
                ),
                data = mapOf("hostId" to hostId.toString())
            )
        )*/
        val intent = Intent().apply {
            action = Intent.ACTION_VIEW
            setClassName(
                this@EventDetailActivity,
                "org.ktc2.cokaen.wouldyouin.feat_profile.view.HostProfileActivity"
            )
            putExtra("hostId", hostId.toString())
        }
        startActivity(intent)
    }
}
