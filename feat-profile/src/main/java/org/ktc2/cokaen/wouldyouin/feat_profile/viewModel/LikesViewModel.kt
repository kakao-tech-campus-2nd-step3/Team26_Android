package org.ktc2.cokaen.wouldyouin.feat_profile.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.ktc2.cokaen.wouldyouin.data.model.MemberType
import org.ktc2.cokaen.wouldyouin.network.repository.LikesAPIRetrofitRepository
import javax.inject.Inject

@HiltViewModel
class LikesViewModel @Inject constructor(
    private val likesRepository: LikesAPIRetrofitRepository,
    application: Application
) : ViewModel() {

    private val _isLiked = MutableStateFlow(false)
    val isLiked = _isLiked.asStateFlow()

    fun checkIfLiked(memberId: Long, memberType: MemberType) {
        viewModelScope.launch {
            try {
                var currentPage = 0
                var lastId = Long.MAX_VALUE
                var found = false

                while (!found) {
                    val response = likesRepository.getLikes(memberType.name, currentPage, 50, lastId)
                    if (response.likes.isEmpty()) break

                    found = response.likes.any { it.memberId == memberId }
                    if (found) {
                        _isLiked.value = true
                        break
                    }

                    lastId = response.likes.last().memberId
                    currentPage++
                }

                if (!found) _isLiked.value = false
            } catch (e: Exception) {
                Log.e("LikesViewModel", "Failed to check like status", e)
            }
        }
    }

    fun toggleLike(curatorId: Long) {
        viewModelScope.launch {
            try {
                val response = likesRepository.postLike(curatorId, MemberType.curator)
                _isLiked.value = response.liked
            } catch (e: Exception) {
                Log.e("LikesViewModel", "Failed to toggle like", e)
            }
        }
    }
}