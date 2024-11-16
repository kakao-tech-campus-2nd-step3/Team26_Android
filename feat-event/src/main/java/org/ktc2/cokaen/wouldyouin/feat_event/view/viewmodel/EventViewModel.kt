package org.ktc2.cokaen.wouldyouin.feat_event.view.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.ktc2.cokaen.wouldyouin.data.model.ApiResponseBodyEventResponse
import org.ktc2.cokaen.wouldyouin.data.model.ApiResponseBodyEventSliceResponse
import org.ktc2.cokaen.wouldyouin.network.repository.EventAPIRetrofitRepository
import javax.inject.Inject

@HiltViewModel
class EventViewModel @Inject constructor(
    private val repository: EventAPIRetrofitRepository
) : ViewModel() {

    //전체 행사 목록 조회
    private val _eventList = MutableLiveData<ApiResponseBodyEventSliceResponse?>()
    val eventList: LiveData<ApiResponseBodyEventSliceResponse?> get() = _eventList

    private val _eventListDetail = MutableLiveData<ApiResponseBodyEventSliceResponse?>()
    val eventListDetail: LiveData<ApiResponseBodyEventSliceResponse?> get() = _eventListDetail

    //단일 행사 상세 조회
    private val _eventDetails = MutableLiveData<ApiResponseBodyEventResponse?>()
    val eventDetails: LiveData<ApiResponseBodyEventResponse?> get() = _eventDetails

    //주최자별 행사 조회
    private val _eventsByHost = MutableLiveData<ApiResponseBodyEventSliceResponse?>()
    val eventsByHost: LiveData<ApiResponseBodyEventSliceResponse?> get() = _eventsByHost

    //전체 행사 목록 조회
    fun fetchEventList(
        startLatitude: Double,
        startLongitude: Double,
        endLatitude: Double,
        endLongitude: Double,
        latitude: Double,
        longitude: Double,
        title: String? = null,
        category: String? = null,
        area: String? = null,
        page: Int = 0,
        size: Int = 10,
        lastId: Long? = null,
        context: Context
    ) {
        viewModelScope.launch {
            Log.d("EventFetch", "Fetching events for category: $category")
            val result = repository.getEventList(
                startLatitude = startLatitude,
                startLongitude = startLongitude,
                endLatitude = endLatitude,
                endLongitude = endLongitude,
                latitude = latitude,
                longitude = longitude,
                title = title,
                category = category,
                area = area,
                page = page,
                size = size,
                lastId = lastId,
                context = context
            )
            _eventList.value = result
            Log.d("EventFetch", "Fetched events: ${result?.data?.events}")
        }
        /*
        viewModelScope.launch {
            _eventList.value = repository.getEventList(
                startLatitude, startLongitude, endLatitude, endLongitude, latitude, longitude, title, category, area, page, size, lastId, context
            )
        }*/
    }

    fun fetchEventListDetail(
        startLatitude: Double,
        startLongitude: Double,
        endLatitude: Double,
        endLongitude: Double,
        latitude: Double,
        longitude: Double,
        title: String? = null,
        category: String? = null,
        area: String? = null,
        page: Int = 0,
        size: Int = 10,
        lastId: Long? = null,
        context: Context
    ) {
        viewModelScope.launch {
            val result = repository.getEventListDetail(
                startLatitude, startLongitude, endLatitude, endLongitude,
                latitude, longitude, title, category, area, page, size, lastId, context
            )
            _eventListDetail.value = result
        }
    }

    //단일 행사 상세 조회
    fun fetchEventDetails(eventId: Long, context: Context) {
        viewModelScope.launch {
            _eventDetails.value = repository.getEventDetails(eventId, context)
        }
    }

    //주최자별 행사 조회
    fun fetchEventsByHost(
        hostId: Long,
        page: Int = 0,
        size: Int = 10,
        lastId: Long? = null,
        context: Context
    ) {
        viewModelScope.launch {
            _eventsByHost.value = repository.getEventsByHost(hostId, page, size, lastId, context)
        }
    }
}


/*
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
}*/
