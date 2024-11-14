package org.ktc2.cokaen.wouldyouin.feat_booking.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import org.ktc2.cokaen.wouldyouin.feat_booking.databinding.ActivityBookingBinding
import org.ktc2.cokaen.wouldyouin.feat_event.view.viewmodel.EventViewModel

class BookingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBookingBinding
    private var totalPrice: Int = 0
    private val eventViewModel: EventViewModel by viewModels()

    // 데이터 받는 변수 부분입니다. 나중에 수정해주세요!
    private var bookingId: String? = null
    private var userId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 데이터 받아오는 부분
        val eventId = intent.getStringExtra("eventId")?.toLongOrNull()
        if (eventId != null) {
            eventViewModel.fetchEventDetails(eventId, this)
        }
        /*
        intent?.data?.let { uri ->
            bookingId = uri.getQueryParameter("bookingId")
            userId = uri.getQueryParameter("userId")
        }*/

        // ViewModel의 eventDetails 관찰하여 UI 업데이트
        eventViewModel.eventDetails.observe(this) { eventResponse ->
            eventResponse?.data?.let { event ->
                binding.eventName.text = event.title
                binding.eventOrganizerName.text = event.host.nickname
                binding.eventLocation.text = event.location.detailAddress
                binding.eventDate.text = event.startTime.toString()
                //binding.price.text = "₩${event.price}"

                // 첫 가격 설정
                updateTotalPrice(1, event.price)
            }
        }


        binding.numberPicker.minValue = 1
        binding.numberPicker.maxValue = 10
        binding.numberPicker.wrapSelectorWheel = false


        binding.numberPicker.setOnValueChangedListener { _, _, newVal ->
            eventViewModel.eventDetails.value?.data?.price?.let { pricePerTicket ->
                updateTotalPrice(newVal, pricePerTicket)
            }
        }

        binding.payButton.setOnClickListener {
            val paymentUrl = "https://www.example.com/payment?amount=$totalPrice"  //임시 URL
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(paymentUrl))
            startActivity(intent)
        }
    }

    private fun updateTotalPrice(quantity: Int, pricePerTicket: Int) {
        totalPrice = quantity * pricePerTicket
        binding.price.text = "₩ $totalPrice"
    }
}