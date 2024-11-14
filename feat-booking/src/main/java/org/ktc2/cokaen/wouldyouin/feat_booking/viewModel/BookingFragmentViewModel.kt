package org.ktc2.cokaen.wouldyouin.feat_booking.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.ktc2.cokaen.wouldyouin.core.ToastUtils
import org.ktc2.cokaen.wouldyouin.data.model.ReservationResponse
import org.ktc2.cokaen.wouldyouin.feat_booking.repository.ReservationRepository
import org.ktc2.cokaen.wouldyouin.network.repository.ReservationAPIRetrofitRepository
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class BookingFragmentViewModel @Inject constructor(
    application: Application,
    private val reservationRepository: ReservationRepository
) : ViewModel() {
    private val context = application.applicationContext

    private var lastId: Long = Long.MAX_VALUE
    private var isLastPage: Boolean = false
    private var currentPage: Int = 0

    private val _bookings = MutableStateFlow<List<ReservationResponse>>(emptyList())
    val bookings: StateFlow<List<ReservationResponse>> = _bookings.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // 변환된 예약 목록을 노출하는 Flow
    // StateFlow를 observe하는 방식으로 변경
    private val _processedBookings = MutableStateFlow<List<ReservationResponse>>(emptyList())
    val processedBookings: StateFlow<List<ReservationResponse>> = _processedBookings.asStateFlow()

    init {
        loadBookings()

        viewModelScope.launch {
            bookings.collect { reservationList ->
                val processed = reservationList
                    .filter { !isEventEnded(it.event.startTime.toString()) }
                    .sortedBy { parseEventDate(it.event.startTime.toString()) }
                    .distinctBy { it.id }
                _processedBookings.value = processed
            }
        }
    }

    fun loadBookings(page: Int = currentPage, size: Int = 10) {
        if (_isLoading.value || isLastPage) return

        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = reservationRepository.getReservationList(page, size, lastId)
                if (response.reservations.isNotEmpty()) {
                    _bookings.update { currentList ->
                        (currentList + response.reservations)
                    }
                    lastId = response.reservations.last().id
                    currentPage++
                } else {
                    isLastPage = true
                }
            } catch (e: Exception) {
                Log.e("BookingViewModel", "Failed to load bookings", e)
                ToastUtils.showShortToast(context, "예매 목록 조회에 실패했습니다. 다시 시도해 주세요.")
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun isEventEnded(eventDate: String): Boolean {
        return try {
            val parsedDate = parseEventDate(eventDate)
            parsedDate.isBefore(LocalDateTime.now())
        } catch (e: Exception) {
            Log.e("BookingViewModel", "Error parsing date: $eventDate", e)
            false
        }
    }

    private fun parseEventDate(dateStr: String): LocalDateTime {
        return try {
            // 여기서는 날짜 형식을 예시로 들었습니다. 실제 데이터의 형식에 맞게 수정해야 합니다.
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            LocalDateTime.parse(dateStr, formatter)
        } catch (e: Exception) {
            Log.e("BookingViewModel", "Error parsing date: $dateStr", e)
            LocalDateTime.now() // 파싱 실패시 현재 시간 반환
        }
    }

    fun refresh() {
        currentPage = 0
        isLastPage = false
        lastId = Long.MAX_VALUE
        viewModelScope.launch {
            _bookings.emit(emptyList())
            loadBookings()
        }
    }
}
