package org.ktc2.cokaen.wouldyouin.feat_booking.viewModel

import android.content.Context
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
            _eventDetails.value = repository.getEventDetails(eventId, context)
        }
    }
}