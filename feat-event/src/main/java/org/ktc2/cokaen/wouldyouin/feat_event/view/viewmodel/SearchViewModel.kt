package org.ktc2.cokaen.wouldyouin.feat_event.view.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.ktc2.cokaen.wouldyouin.data.model.ApiResponseBodyEventSliceResponse
import org.ktc2.cokaen.wouldyouin.data.model.EventResponse
import org.ktc2.cokaen.wouldyouin.network.repository.EventAPIRetrofitRepository
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val repository: EventAPIRetrofitRepository) : ViewModel() {

    private val _eventList = MutableLiveData<ApiResponseBodyEventSliceResponse?>()
    val eventList: LiveData<ApiResponseBodyEventSliceResponse?> get() = _eventList

    fun fetchEventListDetail(
        title: String,
        startLatitude: Double? = null,
        startLongitude: Double? = null,
        endLatitude: Double? = null,
        endLongitude: Double? = null,
        latitude: Double,
        longitude: Double,
        category: String? = null,
        area: String? = null,
        page: Int = 0,
        size: Int = 10,
        lastId: Long? = null,
        context: Context
    ) {
        viewModelScope.launch {
            Log.d("SearchViewModel", "Starting search with title: $title") // 로그 추가
            try {
                val result = repository.getEventListDetail(
                    title = title,
                    startLatitude = startLatitude,
                    startLongitude = startLongitude,
                    endLatitude = endLatitude,
                    endLongitude = endLongitude,
                    latitude = latitude,
                    longitude = longitude,
                    category = category,
                    area = area,
                    page = page,
                    size = size,
                    lastId = lastId,
                    context = context
                )
                Log.d("SearchViewModel", "Search result: $result") // 검색 결과 로그 추가
                _eventList.value = result
                //_eventList.postValue(result)
                Log.d("SearchViewModel", "Fetched event list: ${_eventList.value}")
            } catch (e: Exception) {
                Log.e("SearchViewModel", "Search failed: ${e.message}", e) // 오류 발생 시 로그 추가
            }
        }
    }
}
