package org.ktc2.cokaen.wouldyouin.feat_booking.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.ktc2.cokaen.wouldyouin.core.ToastUtils
import org.ktc2.cokaen.wouldyouin.data.model.ReservationResponse
import org.ktc2.cokaen.wouldyouin.network.repository.ReservationAPIRetrofitRepository
import javax.inject.Inject

@HiltViewModel
class BookingListViewModel @Inject constructor(
    private val bookingRepository: ReservationAPIRetrofitRepository,
    application: Application
): ViewModel() {
    private val context = application.applicationContext

    private var lastId: Long = Long.MAX_VALUE
    private var isLastPage: Boolean = false
    private var currentPage: Int = 0

    private val _reservations = MutableStateFlow<List<ReservationResponse>>(emptyList())
    val reservations: StateFlow<List<ReservationResponse>> = _reservations.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun loadReservations(page: Int = currentPage, size: Int = 10) {
        if (_isLoading.value || isLastPage) return

        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = bookingRepository.getReservationList(page = page, size = size, lastId = lastId)
                if (response.reservations.isNotEmpty()) {
                    _reservations.update { currentList ->
                        (currentList + response.reservations)
                    }
                    lastId = response.reservations.last().id
                    currentPage++
                } else {
                    isLastPage = true
                }
            } catch (e: Exception) {
                Log.e("BookingViewModel", "Failed to load bookings", e)
                ToastUtils.showShortToast(context, "예매 내역 조회에 실패했습니다. 다시 시도해 주세요.")
            } finally {
                _isLoading.value = false
            }
        }
    }
}