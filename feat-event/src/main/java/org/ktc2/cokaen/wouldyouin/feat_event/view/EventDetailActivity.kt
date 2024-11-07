package org.ktc2.cokaen.wouldyouin.feat_event.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapView
import dagger.hilt.android.AndroidEntryPoint
import org.ktc2.cokaen.wouldyouin.core_navigation.ActivityNavigationOptions
import org.ktc2.cokaen.wouldyouin.core_navigation.DeepLinkDestinations
import org.ktc2.cokaen.wouldyouin.core_navigation.NavigationCommand
import org.ktc2.cokaen.wouldyouin.core_navigation.NavigationDestination
import org.ktc2.cokaen.wouldyouin.core_navigation.NavigationUtil
//import org.ktc2.cokaen.wouldyouin.feat_booking.view.BookingActivity
import org.ktc2.cokaen.wouldyouin.feat_event.R
import org.ktc2.cokaen.wouldyouin.feat_event.databinding.ActivityEventDetailBinding
import javax.inject.Inject

@AndroidEntryPoint
class EventDetailActivity : AppCompatActivity() {
    @Inject
    lateinit var navigationUtil: NavigationUtil
    private lateinit var binding: ActivityEventDetailBinding
    private lateinit var mapView: MapView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_event_detail)
        binding.eventdetail = this

        val eventTitle = intent.getStringExtra("event_title")
        val eventStartTime = intent.getStringExtra("event_startTime")
        val eventEndTime = intent.getStringExtra("event_endTime")
        val eventLocation = intent.getStringExtra("event_location")
        val eventPrice = intent.getStringExtra("event_price")
        val eventSeats = intent.getStringExtra("event_totalSeats")
        val eventDescription = intent.getStringExtra("event_description")
        val eventImage = intent.getStringExtra("event_image")

        binding.eventName.text = eventTitle
        binding.eventTime.text = eventStartTime
        binding.eventLocation.text = eventLocation
        binding.eventFee.text = "입장료 ${eventPrice}₩"
        binding.eventSeats.text = "${eventSeats}"
        binding.eventDescription.text = eventDescription

        if (!eventImage.isNullOrEmpty()) {
            Glide.with(this)
                .load(eventImage)
                .into(binding.posterImage)
        }

        binding.bookButton.setOnClickListener {
            //네비게이션 부탁드립니다(import도 주석 처리 했습니다)
            startBookingActivity()
        }

        binding.viewProfile.setOnClickListener {
            startMemberProfileActivity()
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

    private fun startMemberProfileActivity() {
        navigationUtil.navigate(
            NavigationCommand(
                destination = NavigationDestination.Activity(DeepLinkDestinations.MEMBER_PROFILE_ACTIVITY),
                activityOptions = ActivityNavigationOptions(
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK,
                    clearTop = true
                ),
                data = mapOf() // 여기에 필요한 데이터(id) 넘겨주시면 됩니다!!
            )
        )
    }
}
