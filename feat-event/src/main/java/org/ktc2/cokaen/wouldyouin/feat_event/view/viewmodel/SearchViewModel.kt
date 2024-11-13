package org.ktc2.cokaen.wouldyouin.feat_event.view.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.ktc2.cokaen.wouldyouin.data.model.EventResponse
import org.ktc2.cokaen.wouldyouin.network.repository.EventAPIRetrofitRepository
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val repository: EventAPIRetrofitRepository) : ViewModel() {

    private val _eventList = MutableLiveData<List<EventResponse>>()
    val eventList: LiveData<List<EventResponse>> get() = _eventList
    //전체 응답이 필요하다면 EventViewModel을 참고하여 변경할 것

    fun fetchEventList(
        title: String,
        startLatitude: Double,
        startLongitude: Double,
        endLatitude: Double,
        endLongitude: Double,
        latitude: Double,
        longitude: Double,
        context: Context
    ) {
        viewModelScope.launch {
            /*
            val result = repository.searchEvents(
                query = query,
                startLatitude = startLatitude,
                startLongitude = startLongitude,
                endLatitude = endLatitude,
                endLongitude = endLongitude,
                latitude = latitude,
                longitude = longitude,
                context = context
            )
            _eventList.value = result ?: emptyList()*/
            Log.d("SearchViewModel", "Starting search with title: $title") // 로그 추가
            try {
                val result = repository.getEventList(
                    title = title,
                    startLatitude = startLatitude,
                    startLongitude = startLongitude,
                    endLatitude = endLatitude,
                    endLongitude = endLongitude,
                    latitude = latitude,
                    longitude = longitude,
                    context = context
                )
                Log.d("SearchViewModel", "Search result: $result") // 검색 결과 로그 추가
                _eventList.value = result?.data?.events ?: emptyList()
                //전체 응답이 필요하다면 EventViewModel을 참고하여 변경할 것
            } catch (e: Exception) {
                Log.e("SearchViewModel", "Search failed: ${e.message}", e) // 오류 발생 시 로그 추가
            }
        }
    }
}
