package org.ktc2.cokaen.wouldyouin.feat_booking.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.ktc2.cokaen.wouldyouin.core_navigation.ActivityNavigationOptions
import org.ktc2.cokaen.wouldyouin.core_navigation.DeepLinkDestinations
import org.ktc2.cokaen.wouldyouin.core_navigation.NavigationCommand
import org.ktc2.cokaen.wouldyouin.core_navigation.NavigationDestination
import org.ktc2.cokaen.wouldyouin.core_navigation.NavigationUtil
import org.ktc2.cokaen.wouldyouin.data.model.ReservationResponse
import org.ktc2.cokaen.wouldyouin.feat_booking.adapter.BookingAdapter
import org.ktc2.cokaen.wouldyouin.feat_booking.databinding.ActivityBookingListBinding
import org.ktc2.cokaen.wouldyouin.feat_booking.viewModel.BookingListViewModel
import javax.inject.Inject

@AndroidEntryPoint
class BookingListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBookingListBinding
    private val viewModel: BookingListViewModel by viewModels()
    @Inject
    lateinit var navigationUtil: NavigationUtil

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupObservers()
        viewModel.loadReservations() 
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            viewModel.reservations.collect { reservations ->
                setupCurationRecyclerView(reservations)
            }
        }
    }

    private fun setupCurationRecyclerView(reservations: List<ReservationResponse>) {
        val bookingAdapter = BookingAdapter(reservations)
        binding.recyclerView.apply {
            adapter = bookingAdapter
            layoutManager = LinearLayoutManager(
                this@BookingListActivity,
                LinearLayoutManager.VERTICAL,
                false
            )
            addItemDecoration(
                DividerItemDecoration(
                    this@BookingListActivity,
                    DividerItemDecoration.VERTICAL
                )
            )

            bookingAdapter.setOnItemClickListener { reservation ->
                startActivityTo(reservation.id)
            }

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val totalItemCount = layoutManager.itemCount
                    val lastVisibleItem = layoutManager.findLastVisibleItemPosition()

                    if (!viewModel.isLoading.value && totalItemCount <= lastVisibleItem + 5) {
                        viewModel.loadReservations()
                    }
                }
            })
        }
    }

    private fun startActivityTo(reservationId: Long) {
        navigationUtil.navigate(
            NavigationCommand(
                destination = NavigationDestination.Activity(DeepLinkDestinations.DETAIL_CURATION_DEEPLINK),
                activityOptions = ActivityNavigationOptions(
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK,
                    clearTop = true
                ),
                data = mapOf("reservationId" to reservationId.toString())
            )
        )
    }
}