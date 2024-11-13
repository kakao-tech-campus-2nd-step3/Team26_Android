package org.ktc2.cokaen.wouldyouin.feat_curation.viewModel

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
class CurationSearchViewModel @Inject constructor(private val repository: EventAPIRetrofitRepository) : ViewModel() {

    private val _eventList = MutableLiveData<List<EventResponse>>()
    val eventList: LiveData<List<EventResponse>> = _eventList

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    private val _isEmpty = MutableLiveData<Boolean>()
    val isEmpty: LiveData<Boolean> = _isEmpty

    fun searchEvents(
        query: String,
        startLatitude: Double,
        startLongitude: Double,
        endLatitude: Double,
        endLongitude: Double,
        latitude: Double,
        longitude: Double,
        context: Context
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                Log.d("SearchViewModel", "Starting search with query: $query")
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
                Log.d("SearchViewModel", "Search result: $result")
                _eventList.value = result ?: emptyList()
                _isEmpty.value = _eventList.value.isNullOrEmpty()
                Log.d("SearchViewModel", "Search result: ${_isEmpty.value}")
            } catch (e: Exception) {
                Log.e("SearchViewModel", "Search failed: ${e.message}", e)
                _errorMessage.value = "검색 중 오류가 발생했습니다: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}