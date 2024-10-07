package org.ktc2.cokaen.wouldyouin

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import org.ktc2.cokaen.wouldyouin.databinding.ActivityBookingDetailsBinding

class BookingDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBookingDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_booking_details)
    }
}