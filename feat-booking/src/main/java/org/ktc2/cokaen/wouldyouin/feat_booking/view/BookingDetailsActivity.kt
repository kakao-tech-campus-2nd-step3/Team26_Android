package org.ktc2.cokaen.wouldyouin.feat_booking.view

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import dagger.hilt.android.AndroidEntryPoint
import org.ktc2.cokaen.wouldyouin.feat_booking.R
import org.ktc2.cokaen.wouldyouin.feat_booking.databinding.ActivityBookingBinding
import org.ktc2.cokaen.wouldyouin.feat_booking.databinding.ActivityBookingDetailsBinding
import org.ktc2.cokaen.wouldyouin.feat_booking.viewModel.BookingDetailsViewModel

@AndroidEntryPoint
class BookingDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBookingDetailsBinding
    private val viewModel: BookingDetailsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookingDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loadCurationDetail()

        binding.lifecycleOwner = this
        binding.viewModel = viewModel
    }

    private fun loadCurationDetail() {
        val curationId = intent.data?.getQueryParameter("curationId")
        curationId?.let { id ->
            viewModel.loadReservationDetail(id.toLong())
        } ?: run {
            Toast.makeText(this, "큐레이션을 찾을 수 없습니다. 다시 시도해 주세요.", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}