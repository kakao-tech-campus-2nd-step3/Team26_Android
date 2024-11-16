package org.ktc2.cokaen.wouldyouin.feat_booking.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import org.ktc2.cokaen.wouldyouin.core.DateTimeUtils
import org.ktc2.cokaen.wouldyouin.data.model.ReservationCreateRequestWrapper
import org.ktc2.cokaen.wouldyouin.data.model.ReservationRequest
import org.ktc2.cokaen.wouldyouin.feat_booking.databinding.ActivityBookingBinding
import org.ktc2.cokaen.wouldyouin.feat_booking.viewModel.BookingEventViewModel
import org.ktc2.cokaen.wouldyouin.feat_booking.viewModel.ReservationViewModel

@AndroidEntryPoint
class BookingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBookingBinding
    private var totalPrice: Int = 0
    private val bookingEventViewModel: BookingEventViewModel by viewModels()
    private val reservationViewModel: ReservationViewModel by viewModels()

    // 데이터 받는 변수 부분입니다. 나중에 수정해주세요!
    private var bookingId: String? = null
    private var userId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 데이터 받아오는 부분
        val eventId = intent.getStringExtra("eventId")?.toLongOrNull()
        Log.d("BookingActivity", "Received eventId from intent: $eventId")
        if (eventId != null) {
            Log.d("BookingActivity", "Calling fetchEventDetails with eventId: $eventId")
            bookingEventViewModel.fetchEventDetails(eventId, this)
        } else {
            Log.e("BookingActivity", "EventId is null or invalid")
        }

        // ViewModel의 eventDetails 관찰하여 UI 업데이트
        bookingEventViewModel.eventDetails.observe(this) { eventResponse ->
            if (eventResponse == null) {
                Log.e("BookingActivity", "ㅇㅇEvent response is null")
            } else {
                Log.d("BookingActivity", "ㅇㅇEvent fetched successfully: ${eventResponse.data}")
            }
            eventResponse?.data?.let { event ->
                Log.d("BookingActivity", "Event fetched successfully: $event")
                binding.imageUrl = event.images[0]
                binding.eventName.text = event.title
                binding.eventOrganizerName.text = event.host.nickname
                binding.eventLocation.text = event.location.detailAddress
                binding.eventDate.text = DateTimeUtils.formatDateTimeString(event.startTime)
                //binding.price.text = "₩${event.price}"

                // 첫 가격 설정
                updateTotalPrice(1, event.price)
            } ?: run {
                Log.e("BookingActivity", "Event response data is null")
            }
        }

        binding.numberPicker.minValue = 1
        binding.numberPicker.maxValue = 10
        binding.numberPicker.wrapSelectorWheel = false

        binding.numberPicker.setOnValueChangedListener { _, _, newVal ->
            Log.d("BookingActivity", "NumberPicker value changed: $newVal")
            bookingEventViewModel.eventDetails.value?.data?.price?.let { pricePerTicket ->
                updateTotalPrice(newVal, pricePerTicket)
            }
        }

        binding.payButton.setOnClickListener {
            eventId?.let { id ->
                val quantity = binding.numberPicker.value
                val reservationRequest = ReservationRequest(eventId = id, quantity = quantity)
                //val requestWrapper = ReservationCreateRequestWrapper(reservationRequest = reservationRequest)

                Log.d("BookingActivity", "RequestWrapper: $reservationRequest")

                reservationViewModel.createReservation(reservationRequest)

                reservationViewModel.reservationResponse.observe(this) { response ->
                    if (response?.success == true) {
                        Log.d("BookingActivity", "Reservation created successfully: ${response.data}")
                        val reservationId = response.data?.id
                        if (reservationId != null) {
                            reservationId?.let {
                                Log.d("BookingActivity", "Navigating to BookingDetailsActivity with reservationId: $reservationId")
                                val intent = Intent(this, BookingDetailsActivity::class.java).apply {
                                    putExtra("reservationId", reservationId)
                                }
                                startActivity(intent)
                            }
                        } else {
                            Log.e("BookingActivity", "ReservationId is null")
                        }
                    } else {
                        Log.e("BookingActivity", "예매 생성 실패: ${response?.message}")
                        Log.e("BookingActivity", "Response Code: ${response?.code}")
                        Log.e("BookingActivity", "Response Body: ${response?.message}")
                    }
                }
            }
        }
    }

    private fun updateTotalPrice(quantity: Int, pricePerTicket: Int) {
        totalPrice = quantity * pricePerTicket
        Log.d("BookingActivity", "Updating total price: $totalPrice")
        binding.price.text = "₩ $totalPrice"
    }
}