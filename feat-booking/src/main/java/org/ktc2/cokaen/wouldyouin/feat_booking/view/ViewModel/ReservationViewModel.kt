package org.ktc2.cokaen.wouldyouin.feat_booking.view.ViewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.ktc2.cokaen.wouldyouin.data.model.ReservationRequest
import org.ktc2.cokaen.wouldyouin.data.model.ReservationResponse
import org.ktc2.cokaen.wouldyouin.network.repository.ReservationAPIRetrofitRepository
import javax.inject.Inject

class ReservationViewModel @Inject constructor(
    private val repository: ReservationAPIRetrofitRepository
) : ViewModel() {
    // 예약 목록을 저장하기 위한 LiveData
    private val _reservationList = MutableLiveData<List<ReservationResponse>>()
    val reservationList: LiveData<List<ReservationResponse>> get() = _reservationList

    // 특정 예약의 상세 정보를 저장하기 위한 LiveData
    private val _reservationDetails = MutableLiveData<ReservationResponse>()
    val reservationDetails: LiveData<ReservationResponse> get() = _reservationDetails

    // 작업 성공 여부를 저장하기 위한 LiveData
    private val _operationSuccess = MutableLiveData<Boolean>()
    val operationSuccess: LiveData<Boolean> get() = _operationSuccess

    // 전체 예약 목록을 가져오는 메서드
    fun fetchReservationList(context: Context) {
        viewModelScope.launch {
            _reservationList.value = repository.getReservationList(context)
        }
    }

    // 특정 예약의 상세 정보를 가져오는 메서드
    fun fetchReservationDetails(reservationId: String, context: Context) {
        viewModelScope.launch {
            _reservationDetails.value = repository.getReservationDetails(reservationId, context)
        }
    }

    // 새로운 예약을 생성하는 메서드
    fun createReservation(reservationRequest: ReservationRequest, context: Context) {
        viewModelScope.launch {
            _operationSuccess.value = repository.createReservation(reservationRequest, context) != null
        }
    }

    // 예약을 취소하는 메서드
    fun cancelReservation(reservationId: String, context: Context) {
        viewModelScope.launch {
            _operationSuccess.value = repository.cancelReservation(reservationId, context)
        }
    }
}