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
import org.ktc2.cokaen.wouldyouin.data.model.ReservationResponse
import org.ktc2.cokaen.wouldyouin.network.repository.ReservationAPIRetrofitRepository
import javax.inject.Inject

@HiltViewModel
class BookingDetailsViewModel @Inject constructor(
    val reservationRepository: ReservationAPIRetrofitRepository,
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
                val reservationDetail = reservationRepository.getReservationDetails(reservationId)

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

    fun deleteReservation(reservationId: Long) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val success = reservationRepository.deleteReservation(reservationId)
                if (success) {
                    ToastUtils.showShortToast(context, "예매가 취소되었습니다.")
                } else {
                    ToastUtils.showShortToast(context, "예매 취소에 실패했습니다. 다시 시도해 주세요")
                }
            } catch (e: Exception) {
                ToastUtils.showShortToast(context, e.message ?: "예매 취소에 실패했습니다. 다시 시도해 주세요")
            } finally {
                _isLoading.value = false
            }
        }
    }
}