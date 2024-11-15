package org.ktc2.cokaen.wouldyouin.feat_booking.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewpager2.widget.ViewPager2
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.ktc2.cokaen.wouldyouin.core_navigation.ActivityNavigationOptions
import org.ktc2.cokaen.wouldyouin.core_navigation.DeepLinkDestinations
import org.ktc2.cokaen.wouldyouin.core_navigation.NavigationCommand
import org.ktc2.cokaen.wouldyouin.core_navigation.NavigationDestination
import org.ktc2.cokaen.wouldyouin.core_navigation.NavigationUtil
import org.ktc2.cokaen.wouldyouin.data.model.Location
import org.ktc2.cokaen.wouldyouin.data.model.ReservationEventResponse
import org.ktc2.cokaen.wouldyouin.data.model.ReservationMemberResponse
import org.ktc2.cokaen.wouldyouin.data.model.ReservationResponse
import org.ktc2.cokaen.wouldyouin.feat_booking.R
import org.ktc2.cokaen.wouldyouin.feat_booking.adapter.BookingViewPagerAdapter
import org.ktc2.cokaen.wouldyouin.feat_booking.databinding.FragmentBookingBinding
import org.ktc2.cokaen.wouldyouin.feat_booking.viewModel.BookingFragmentViewModel
import javax.inject.Inject

@AndroidEntryPoint
class BookingFragment : Fragment() {
    private var _binding: FragmentBookingBinding? = null
    private val binding get() = _binding!!

    private val viewModel: BookingFragmentViewModel by viewModels()

    private val bookingAdapter = BookingViewPagerAdapter()

    @Inject
    lateinit var navigationUtil: NavigationUtil

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
        observeBookings()
    }

    override fun onDestroyView() {
        binding.viewPager.unregisterOnPageChangeCallback(
            object : ViewPager2.OnPageChangeCallback() {}
        )
        _binding = null
        super.onDestroyView()
    }

    private fun setupViewPager() {
        binding.viewPager.apply {
            adapter = bookingAdapter

            bookingAdapter.setOnItemClickListener { reservation ->
                startBookingDetailsActivity(reservation.id)
            }

            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    // 마지막 페이지에서 2페이지 전에 도달하면 다음 데이터 로드
                    if (position >= bookingAdapter.currentList.size - 2) {
                        viewModel.loadBookings()
                    }
                }
            })
        }

        binding.dotsIndicator.setViewPager2(binding.viewPager)
    }

    private fun observeBookings() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.processedBookings.collect { bookings ->
                    if (bookings.isEmpty()) {
                        binding.viewPager.visibility = View.GONE
                        binding.dotsIndicator.visibility = View.GONE
                        binding.emptyView.visibility = View.VISIBLE
                    } else {
                        binding.viewPager.visibility = View.VISIBLE
                        binding.dotsIndicator.visibility = View.VISIBLE
                        binding.emptyView.visibility = View.GONE
                        bookingAdapter.submitList(bookings) {
                            binding.dotsIndicator.setViewPager2(binding.viewPager)
                        }
                    }
                }
            }
        }
    }

    private fun startBookingDetailsActivity(reservationId: Long) {
        navigationUtil.navigate(
            NavigationCommand(
                destination = NavigationDestination.Activity(DeepLinkDestinations.DETAIL_BOOKING_ACTIVITY),
                activityOptions = ActivityNavigationOptions(
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK,
                    clearTop = true
                ),
                data = mapOf("reservationId" to reservationId.toString())
            )
        )
    }
}

//    private fun setupViewPager() {
//        val mockReservations = listOf(
//            ReservationResponse(
//                id = 1L,
//                member = ReservationMemberResponse(
//                    id = 101L,
//                    email = "john.doe@example.com",
//                    nickname = "JohnD",
//                    phone = "010-1234-5678",
//                    gender = "Male"
//                ),
//                event = ReservationEventResponse(
//                    id = 201L,
//                    date = "2024-12-01T14:00:00",
//                    title = "Music Concert",
//                    price = 50000,
//                    location = Location("광주 문화의전당", latitude = 37.5665, longitude = 126.9780),
//                    imageUrl = "https://img1.bizhows.com/bhfile01/__CM_FILE_DATA/201911/20/18/1481577_1574242984817.jpg"
//                ),
//                price = 50000,
//                quantity = 2,
//                reservationDate = "2024-11-10T18:30:00"
//            ),
//            ReservationResponse(
//                id = 2L,
//                member = ReservationMemberResponse(
//                    id = 102L,
//                    email = "jane.smith@example.com",
//                    nickname = "JaneS",
//                    phone = "010-9876-5432",
//                    gender = "Female"
//                ),
//                event = ReservationEventResponse(
//                    id = 202L,
//                    date = "2024-12-15T19:00:00",
//                    title = "Art Exhibition",
//                    price = 30000,
//                    location = Location("광주 문화의전당", latitude = 37.5665, longitude = 126.9780),
//                    imageUrl = "https://img1.bizhows.com/bhfile01/__CM_FILE_DATA/201911/20/18/1481577_1574242984817.jpg"
//                ),
//                price = 60000,
//                quantity = 2,
//                reservationDate = "2024-11-12T12:00:00"
//            ),
//            ReservationResponse(
//                id = 3L,
//                member = ReservationMemberResponse(
//                    id = 103L,
//                    email = "sam.wilson@example.com",
//                    nickname = "SamW",
//                    phone = "010-2222-3333",
//                    gender = "Male"
//                ),
//                event = ReservationEventResponse(
//                    id = 203L,
//                    date = "2024-12-20T18:00:00",
//                    title = "Food Festival",
//                    price = 20000,
//                    location = Location("광주 문화의전당", latitude = 37.5665, longitude = 126.9780),
//                    imageUrl = "https://img1.bizhows.com/bhfile01/__CM_FILE_DATA/201911/20/18/1481577_1574242984817.jpg"
//                ),
//                price = 40000,
//                quantity = 2,
//                reservationDate = "2024-11-15T16:45:00"
//            )
//        )
//
//
//        bookingAdapter = BookingViewPagerAdapter(emptyList())
//        binding.viewPager.adapter = bookingAdapter
//        binding.dotsIndicator.attachTo(binding.viewPager)
//
//        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
//            override fun onPageSelected(position: Int) {
//                super.onPageSelected(position)
//                // 마지막 페이지에 가까워지면 다음 페이지 로드
//                if (position >= bookingAdapter.itemCount - 2) {
//                    viewModel.loadBookings()
//                }
//            }
//        })
//    }