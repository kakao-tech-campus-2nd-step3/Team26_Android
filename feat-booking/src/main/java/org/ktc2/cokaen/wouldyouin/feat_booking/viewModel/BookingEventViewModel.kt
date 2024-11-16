package org.ktc2.cokaen.wouldyouin.feat_booking.viewModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.ktc2.cokaen.wouldyouin.data.model.ApiResponseBodyEventResponse
import org.ktc2.cokaen.wouldyouin.network.repository.EventAPIRetrofitRepository
import javax.inject.Inject

@HiltViewModel
class BookingEventViewModel @Inject constructor(
    private val repository: EventAPIRetrofitRepository
) : ViewModel() {

    private val _eventDetails = MutableLiveData<ApiResponseBodyEventResponse?>()
    val eventDetails: LiveData<ApiResponseBodyEventResponse?> get() = _eventDetails

    fun fetchEventDetails(eventId: Long, context: Context) {
        viewModelScope.launch {
            //_eventDetails.value = repository.getEventDetails(eventId, context)
            val eventDetails = repository.getEventDetails(eventId, context)
            if (eventDetails != null) {
                Log.d("BookingEventViewModel", "Event details fetched: $eventDetails")
            } else {
                Log.e("BookingEventViewModel", "Failed to fetch event details.")
            }
            _eventDetails.value = eventDetails
        }
    }
}