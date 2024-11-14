package org.ktc2.cokaen.wouldyouin.feat_event.view.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.ktc2.cokaen.wouldyouin.data.model.ApiResponseBodyListAdvertisementResponse
import org.ktc2.cokaen.wouldyouin.network.repository.AdAPIRetrofitRepository
import javax.inject.Inject

@HiltViewModel
class AdViewModel @Inject constructor(
    private val repository: AdAPIRetrofitRepository
) : ViewModel() {
    private val _adList = MutableLiveData<ApiResponseBodyListAdvertisementResponse?>()
    val adList: LiveData<ApiResponseBodyListAdvertisementResponse?> get() = _adList

    fun fetchAdList(context: Context) {
        viewModelScope.launch {
            val response = repository.getAdList(context)
            _adList.postValue(response)
        }
    }
}