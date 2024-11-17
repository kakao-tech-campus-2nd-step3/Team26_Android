package com.example.feat_likes.viewModel

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
import org.ktc2.cokaen.wouldyouin.data.model.LikeResponse
import org.ktc2.cokaen.wouldyouin.data.model.LikeToggleResponse
import org.ktc2.cokaen.wouldyouin.data.model.MemberType
import org.ktc2.cokaen.wouldyouin.data.model.ReservationResponse
import org.ktc2.cokaen.wouldyouin.network.repository.LikesAPIRetrofitRepository
import javax.inject.Inject

@HiltViewModel
class CuratorLikesViewModel @Inject constructor(
    private val likesRepository: LikesAPIRetrofitRepository,
    application: Application
): ViewModel() {
    private val context = application.applicationContext
    private val _likeState = MutableStateFlow<LikeToggleResponse?>(null)
    val likeState: StateFlow<LikeToggleResponse?> = _likeState

    private val _likesList = MutableStateFlow<List<LikeResponse>>(emptyList())
    val likesList: StateFlow<List<LikeResponse>> = _likesList

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private var lastId: Long = Long.MAX_VALUE
    private var isLastPage: Boolean = false
    private var currentPage: Int = 0

    private val _bookings = MutableStateFlow<List<ReservationResponse>>(emptyList())
    val bookings: StateFlow<List<ReservationResponse>> = _bookings.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadLikes()
    }
    fun loadLikes(page: Int = currentPage, size: Int = 10) {
        if (_isLoading.value || isLastPage) return

        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = likesRepository.getLikes(type = MemberType.curator.name, page = page, size = size, lastId = lastId)
                if (response.likes.isNotEmpty()) {
                    _likesList.update { currentList ->
                        (currentList + response.likes)
                    }
                    lastId = response.likes.last().memberId
                    currentPage++
                } else {
                    isLastPage = true
                }
            } catch (e: Exception) {
                Log.e("CuratorLikes", "Failed to load bookings", e)
                ToastUtils.showShortToast(context, "좋아요한 큐레이터 목록 조회에 실패했습니다. 다시 시도해 주세요.")
            } finally {
                _isLoading.value = false
            }
        }
    }

    suspend fun postLike(targetMemberId: Long, type: MemberType): Boolean {
        return try {
            val result = likesRepository.postLike(targetMemberId, type)
            _likeState.value = result
            true // 요청 성공 시 true 반환
        } catch (e: Exception) {
            _errorMessage.value = e.message
            false // 요청 실패 시 false 반환
        }
    }
}

