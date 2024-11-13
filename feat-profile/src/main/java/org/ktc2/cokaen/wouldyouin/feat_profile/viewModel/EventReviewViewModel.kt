package org.ktc2.cokaen.wouldyouin.feat_profile.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.ktc2.cokaen.wouldyouin.data.model.ReviewCreateRequest
import org.ktc2.cokaen.wouldyouin.feat_profile.repository.EventReviewRepository
import org.ktc2.cokaen.wouldyouin.network.repository.ServerCommonAPIRetrofitRepository
import javax.inject.Inject

data class PendingReview(
    val eventId: Long,
    val eventName: String,
    val eventDate: String,
    val eventImageUrl: String? = null
)

@HiltViewModel
class EventReviewViewModel @Inject constructor(
    private val repository: EventReviewRepository
) : ViewModel() {

    private val _pendingReviews = MutableStateFlow<List<PendingReview>>(emptyList())
    val pendingReviews = _pendingReviews.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    private val _reviewSubmitResult = MutableSharedFlow<Boolean>()
    val reviewSubmitResult = _reviewSubmitResult.asSharedFlow()

    fun submitReview(review: ReviewCreateRequest) {
        viewModelScope.launch {
            _loading.value = true
            try {
                val response = repository.submitReview(review)
                // 성공적으로 리뷰가 등록되면 pending 목록에서 제거
                removePendingReview(review.eventId)
                _reviewSubmitResult.emit(true)
            } catch (e: ServerCommonAPIRetrofitRepository.CustomException) {
                _error.value = e.message
                _reviewSubmitResult.emit(false)
            } catch (e: Exception) {
                _error.value = "알 수 없는 오류가 발생했습니다"
                _reviewSubmitResult.emit(false)
            } finally {
                _loading.value = false
            }
        }
    }

    private fun removePendingReview(eventId: Long) {
        val currentList = _pendingReviews.value.toMutableList()
        currentList.removeAll { it.eventId == eventId }
        _pendingReviews.value = currentList
    }
}