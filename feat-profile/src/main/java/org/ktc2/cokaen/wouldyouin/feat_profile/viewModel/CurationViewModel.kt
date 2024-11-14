package org.ktc2.cokaen.wouldyouin.feat_profile.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.ktc2.cokaen.wouldyouin.core.ToastUtils
import org.ktc2.cokaen.wouldyouin.data.model.CurationResponse
import org.ktc2.cokaen.wouldyouin.network.repository.CurationAPIRetrofitRepository
import javax.inject.Inject

@HiltViewModel
class CurationViewModel @Inject constructor(
    private val curationRepository: CurationAPIRetrofitRepository,
    application: Application
): ViewModel() {
    private val context = application.applicationContext

    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    private var lastId: Long = Long.MAX_VALUE
    private var isLastPage: Boolean = false
    private var currentPage: Int = 0

    private val _curations = MutableStateFlow<List<CurationResponse>>(emptyList())
    val curations: StateFlow<List<CurationResponse>> = _curations.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun loadCurations(curatorId: Long, page: Int = currentPage, size: Int = 10) {
        if (_isLoading.value || isLastPage) return

        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = curationRepository.getCurationsByCurator(curationId = curatorId, page = page, size = size, lastId = lastId)
                if (response.curations.isNotEmpty()) {
                    _curations.update { currentList ->
                        (currentList + response.curations)
                    }
                    lastId = response.curations.last().id
                    currentPage++
                } else {
                    isLastPage = true
                }
            } catch (e: Exception) {
                Log.e("BookingViewModel", "Failed to load bookings", e)
                ToastUtils.showShortToast(context, "좋아요한 주최자 목록 조회에 실패했습니다. 다시 시도해 주세요.")
            } finally {
                _isLoading.value = false
            }
        }
    }
}