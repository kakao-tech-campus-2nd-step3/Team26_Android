package org.ktc2.cokaen.wouldyouin.feat_event.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapView
import dagger.hilt.android.AndroidEntryPoint
import org.ktc2.cokaen.wouldyouin.core_navigation.ActivityNavigationOptions
import org.ktc2.cokaen.wouldyouin.core_navigation.DeepLinkDestinations
import org.ktc2.cokaen.wouldyouin.core_navigation.NavigationCommand
import org.ktc2.cokaen.wouldyouin.core_navigation.NavigationDestination
import org.ktc2.cokaen.wouldyouin.core_navigation.NavigationUtil
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
                binding.eventTime.text = event.data?.startTime
                binding.eventDuration.text = "약 ${event.data?.endTime} - ${event.data?.startTime}분"
                binding.eventLocation.text = event.data?.location?.detailAddress
                binding.eventFee.text = "입장료 ₩${event.data?.price}"
                binding.eventSeats.text = "${event.data?.leftSeat}/${event.data?.totalSeat}"
                binding.eventDescription.text = event.data?.content

                //행사 위치 정보 설정
                eventLatitude = event.data?.location?.latitude
                eventLongitude = event.data?.location?.longitude

                //주최자 정보 설정
                binding.organizerName.text = event.data?.host?.nickname
                binding.organizerInfo.text = event.data?.host?.intro
                binding.eventDescriptionTitle.text = event.data?.content
                binding.contactPhone.text = event.data?.host?.phone
                binding.contactEmail.text = event.data?.host?.email

                binding.posterImageUrl = event.data?.images?.firstOrNull()
                binding.organizerImageUrl = event.data?.host?.profileImageUrl
            }
        }

        /*
        val eventTitle = intent.getStringExtra("event_title")
        val eventStartTime = intent.getStringExtra("event_startTime")
        val eventEndTime = intent.getStringExtra("event_endTime")
        val eventLocation = intent.getStringExtra("event_location")
        val eventPrice = intent.getStringExtra("event_price")
        val eventSeats = intent.getStringExtra("event_totalSeats")
        val eventDescription = intent.getStringExtra("event_description")
        val eventImage = intent.getStringExtra("event_image")

        binding.eventName.text = eventTitle
        binding.eventTime.text = "$eventStartTime - $eventEndTime"
        binding.eventLocation.text = eventLocation
        binding.eventFee.text = "입장료 ${eventPrice}₩"
        binding.eventSeats.text = "${eventSeats}"
        binding.eventDescription.text = eventDescription

        if (!eventImage.isNullOrEmpty()) {
            Glide.with(this)
                .load(eventImage)
                .into(binding.posterImage)
        }*/

        binding.bookButton.setOnClickListener {
            startBookingActivity()
        }

        binding.viewProfile.setOnClickListener {
            val hostId = eventViewModel.eventDetails.value?.data?.host?.hostId
            if (hostId != null) {
                startMemberProfileActivity(hostId)
            }
        }

        // 지도 카드(MapFragment)에서 전달받은 데이터 사용 시
        /*
        val placeName = intent.getStringExtra("place_name")
        binding.placeName.text = placeName
         */

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
                //행사 위치로 카메라 이동
                eventLatitude?.let { latitude ->
                    eventLongitude?.let { longitude ->
                        kakaoMap.moveCamera(
                            com.kakao.vectormap.camera.CameraUpdateFactory.newCenterPosition(
                                LatLng.from(latitude, longitude), 15
                            )
                        )
                    }
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        mapView.resume()
    }

    override fun onPause() {
        super.onPause()
        mapView.pause()
    }

    private fun startBookingActivity() {
        navigationUtil.navigate(
            NavigationCommand(
                destination = NavigationDestination.Activity(DeepLinkDestinations.PAYMENT_CHECK_ACTIVITY),
                activityOptions = ActivityNavigationOptions(
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK,
                    clearTop = true
                ),
                data = mapOf(
                    "bookingId" to "123",
                    "userId" to "456"
                ) // 여기에 필요한 데이터(id) 넘겨주시면 됩니다!!
            )
        )
    }

    private fun startMemberProfileActivity(hostId: Long) {
        navigationUtil.navigate(
            NavigationCommand(
                destination = NavigationDestination.Activity(DeepLinkDestinations.HOST_PROFILE_ACTIVITY),
                activityOptions = ActivityNavigationOptions(
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK,
                    clearTop = true
                ),
                data = mapOf("hostId" to hostId.toString()) // 여기에 필요한 데이터(id) 넘겨주시면 됩니다!!
            )
        )
    }
}
