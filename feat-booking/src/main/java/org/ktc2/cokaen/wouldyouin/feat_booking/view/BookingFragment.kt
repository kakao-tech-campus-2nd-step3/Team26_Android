package org.ktc2.cokaen.wouldyouin.feat_booking.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.ktc2.cokaen.wouldyouin.data.model.Location
import org.ktc2.cokaen.wouldyouin.data.model.ReservationEventResponse
import org.ktc2.cokaen.wouldyouin.data.model.ReservationMemberResponse
import org.ktc2.cokaen.wouldyouin.data.model.ReservationResponse
import org.ktc2.cokaen.wouldyouin.feat_booking.R
import org.ktc2.cokaen.wouldyouin.feat_booking.adapter.BookingViewPagerAdapter
import org.ktc2.cokaen.wouldyouin.feat_booking.databinding.FragmentBookingBinding

class BookingFragment : Fragment() {
    private var _binding: FragmentBookingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBookingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("test", "booking")
        setupViewPager()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupViewPager() {
        val mockReservations = listOf(
            ReservationResponse(
                id = 1L,
                member = ReservationMemberResponse(
                    id = 101L,
                    email = "john.doe@example.com",
                    nickname = "JohnD",
                    phone = "010-1234-5678",
                    gender = "Male"
                ),
                event = ReservationEventResponse(
                    id = 201L,
                    date = "2024-12-01T14:00:00",
                    title = "Music Concert",
                    price = 50000,
                    location = Location("광주 문화의전당", latitude = 37.5665, longitude = 126.9780),
                    imageUrl = "https://img1.bizhows.com/bhfile01/__CM_FILE_DATA/201911/20/18/1481577_1574242984817.jpg"
                ),
                price = 50000,
                quantity = 2,
                reservationDate = "2024-11-10T18:30:00"
            ),
            ReservationResponse(
                id = 2L,
                member = ReservationMemberResponse(
                    id = 102L,
                    email = "jane.smith@example.com",
                    nickname = "JaneS",
                    phone = "010-9876-5432",
                    gender = "Female"
                ),
                event = ReservationEventResponse(
                    id = 202L,
                    date = "2024-12-15T19:00:00",
                    title = "Art Exhibition",
                    price = 30000,
                    location = Location("광주 문화의전당", latitude = 37.5665, longitude = 126.9780),
                    imageUrl = "https://img1.bizhows.com/bhfile01/__CM_FILE_DATA/201911/20/18/1481577_1574242984817.jpg"
                ),
                price = 60000,
                quantity = 2,
                reservationDate = "2024-11-12T12:00:00"
            ),
            ReservationResponse(
                id = 3L,
                member = ReservationMemberResponse(
                    id = 103L,
                    email = "sam.wilson@example.com",
                    nickname = "SamW",
                    phone = "010-2222-3333",
                    gender = "Male"
                ),
                event = ReservationEventResponse(
                    id = 203L,
                    date = "2024-12-20T18:00:00",
                    title = "Food Festival",
                    price = 20000,
                    location = Location("광주 문화의전당", latitude = 37.5665, longitude = 126.9780),
                    imageUrl = "https://img1.bizhows.com/bhfile01/__CM_FILE_DATA/201911/20/18/1481577_1574242984817.jpg"
                ),
                price = 40000,
                quantity = 2,
                reservationDate = "2024-11-15T16:45:00"
            )
        )


        val adapter = BookingViewPagerAdapter(mockReservations)
        binding.viewPager.adapter = adapter
        binding.dotsIndicator.attachTo(binding.viewPager)
    }
}