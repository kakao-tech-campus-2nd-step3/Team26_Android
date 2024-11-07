package org.ktc2.cokaen.wouldyouin.feat_booking.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import org.ktc2.cokaen.wouldyouin.feat_booking.databinding.ActivityBookingBinding

class BookingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBookingBinding
    private var totalPrice: Int = 0

    // 데이터 받는 변수 부분입니다. 나중에 수정해주세요!
    private var bookingId: String? = null
    private var userId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 데이터 받아오는 부분
        intent?.data?.let { uri ->
            bookingId = uri.getQueryParameter("bookingId")
            userId = uri.getQueryParameter("userId")
        }


        binding.numberPicker.minValue = 1
        binding.numberPicker.maxValue = 10
        binding.numberPicker.wrapSelectorWheel = false

        val pricePerTicket = 5000  //데이터 받아오는 코드로 추후 수정 필요
        updateTotalPrice(1, pricePerTicket)

        binding.numberPicker.setOnValueChangedListener { _, _, newVal ->
            updateTotalPrice(newVal, pricePerTicket)
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