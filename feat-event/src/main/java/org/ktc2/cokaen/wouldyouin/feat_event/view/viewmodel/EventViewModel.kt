package org.ktc2.cokaen.wouldyouin.feat_event.view.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.ktc2.cokaen.wouldyouin.data.model.EventRequest
import org.ktc2.cokaen.wouldyouin.data.model.EventResponse
import org.ktc2.cokaen.wouldyouin.network.repository.EventAPIRetrofitRepository
import javax.inject.Inject

class EventViewModel @Inject constructor(
    private val repository: EventAPIRetrofitRepository
) : ViewModel() {
    // 행사 목록을 저장하기 위한 LiveData
    private val _eventList = MutableLiveData<List<EventResponse>>()
    val eventList: LiveData<List<EventResponse>> get() = _eventList

    // 특정 행사의 상세 정보를 저장하기 위한 LiveData
    private val _eventDetails = MutableLiveData<EventResponse>()
    val eventDetails: LiveData<EventResponse> get() = _eventDetails

    // 작업 성공 여부를 저장하기 위한 LiveData
    private val _operationSuccess = MutableLiveData<Boolean>()
    val operationSuccess: LiveData<Boolean> get() = _operationSuccess

    // 전체 행사 목록을 가져오는 메서드
    fun fetchEventList(context: Context) {
        viewModelScope.launch {
            _eventList.value = repository.getEventList(context)
        }
    }

    // 특정 행사의 상세 정보를 가져오는 메서드
    fun fetchEventDetails(eventId: String, context: Context) {
        viewModelScope.launch {
            _eventDetails.value = repository.getEventDetails(eventId, context)
        }
    }

    // 새로운 행사를 생성하는 메서드
    fun createEvent(eventRequest: EventRequest, context: Context) {
        viewModelScope.launch {
            _operationSuccess.value = repository.createEvent(eventRequest, context) != null
        }
    }

    // 행사를 삭제하는 메서드
    fun deleteEvent(eventId: String, context: Context) {
        viewModelScope.launch {
            _operationSuccess.value = repository.deleteEvent(eventId, context)
        }
    }

    // 행사를 수정하는 메서드
    fun updateEvent(eventId: String, eventRequest: EventRequest, context: Context) {
        viewModelScope.launch {
            _operationSuccess.value = repository.updateEvent(eventId, eventRequest, context) != null
        }
    }
}