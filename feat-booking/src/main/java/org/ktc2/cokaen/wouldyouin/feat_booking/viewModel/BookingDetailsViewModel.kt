package org.ktc2.cokaen.wouldyouin.feat_booking.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.ktc2.cokaen.wouldyouin.core.ToastUtils
import org.ktc2.cokaen.wouldyouin.data.model.CurationCardResponse
import org.ktc2.cokaen.wouldyouin.data.model.CurationResponse
import org.ktc2.cokaen.wouldyouin.data.model.ReservationResponse
import org.ktc2.cokaen.wouldyouin.feat_booking.repository.ReservationRepository
import javax.inject.Inject

@HiltViewModel
class BookingDetailsViewModel @Inject constructor(
    val reservationRepository: ReservationRepository,
    application: Application
): ViewModel() {
    private val context = application.applicationContext

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _reservation = MutableLiveData<ReservationResponse?>()
    val reservation: LiveData<ReservationResponse?> = _reservation

    fun loadReservationDetail(reservationId: Long) {
        if (isLoading.value == true) return

        _isLoading.value = true
        viewModelScope.launch {
            try {
                val reservationDetail = reservationRepository.getReservation(reservationId)

                // CurationResponse 값을 변수에 적용
                _reservation.value = reservationDetail
            } catch (e: Exception) {
                ToastUtils.showShortToast(context, e.message ?: "예매 상세 정보 조회에 실패했습니다. 다시 시도해주세요.")
                Log.e("ReservationDetail", "Error loading curation detail", e)
            } finally {
                _isLoading.value = false
            }
        }
    }
}