package org.ktc2.cokaen.wouldyouin.feat_booking.view

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import dagger.hilt.android.AndroidEntryPoint
import org.ktc2.cokaen.wouldyouin.core.DateTimeUtils
import org.ktc2.cokaen.wouldyouin.feat_booking.R
import org.ktc2.cokaen.wouldyouin.feat_booking.databinding.ActivityBookingDetailsBinding
import org.ktc2.cokaen.wouldyouin.feat_booking.viewModel.BookingDetailsViewModel

@AndroidEntryPoint
class BookingDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBookingDetailsBinding
    private val viewModel: BookingDetailsViewModel by viewModels()

    var reservationId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookingDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        //loadCurationDetail()
        val reservationId = intent.getLongExtra("reservationId", -1L)
        if (reservationId != -1L) {
            viewModel.loadReservationDetail(reservationId)
        } else {
            Toast.makeText(this, "예매 내역을 찾을 수 없습니다. 다시 시도해 주세요.", Toast.LENGTH_SHORT).show()
            finish()
        }

        viewModel.reservation.observe(this, Observer { reservation ->
            reservation?.let {
                binding.imageUrl = reservation.event.thumbnailUrl
                binding.eventName.text = reservation.event.title
                binding.eventLocation.text = reservation.event.location.detailAddress
                binding.eventDate.text = DateTimeUtils.formatDateTimeString(reservation.event.startTime)
                binding.paymentDate.text = DateTimeUtils.formatDateTimeString(reservation.reservationDate)
                binding.paymentAmount.text = "₩${reservation.price}"
                binding.reservationNumber.text = reservation.id.toString()
                binding.bookerName.text = reservation.member.nickname
                binding.ticketQuantity.text = reservation.quantity.toString()
            }
        })

        binding.cancelButton.setOnClickListener {
            viewModel.deleteReservation(reservationId)
            finish()
        }

        /*
        binding.cancelButton.setOnClickListener {
            try {
                reservationId?.toLong()?.let { it1 -> viewModel.deleteReservation(it1) }
            } catch (_: Exception) {
                Toast.makeText(this, "잘못된 접근입니다. 다시 시도해 주세요.", Toast.LENGTH_SHORT).show()
            }

        }*/
    }

    /*
    private fun loadCurationDetail() {
        reservationId = intent.data?.getQueryParameter("reservationID")
        reservationId?.let { id ->
            viewModel.loadReservationDetail(id.toLong())
        } ?: run {
            Toast.makeText(this, "예매 내역을 찾을 수 없습니다. 다시 시도해 주세요.", Toast.LENGTH_SHORT).show()
            finish()
        }
    }*/
}