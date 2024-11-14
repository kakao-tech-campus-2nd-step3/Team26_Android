package com.example.feat_likes.repository

import org.ktc2.cokaen.wouldyouin.data.model.LikeSliceResponse
import org.ktc2.cokaen.wouldyouin.data.model.LikeToggleResponse
import org.ktc2.cokaen.wouldyouin.network.repository.CurationAPIRetrofitRepository
import org.ktc2.cokaen.wouldyouin.network.repository.EventAPIRetrofitRepository
import org.ktc2.cokaen.wouldyouin.network.repository.LikesAPIRetrofitRepository
import org.ktc2.cokaen.wouldyouin.network.repository.ServerCommonAPIRetrofitRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LikesRepository @Inject constructor(
    private val likesRepository: LikesAPIRetrofitRepository
) {
    suspend fun getLikes(type: String, page: Int = 0, size: Int = 10, lastId: Long = Long.MAX_VALUE): LikeSliceResponse {
        return likesRepository.getLikes(type, page, size, lastId)
    }
    suspend fun postLike(targetMemberId: Long): LikeToggleResponse {
        return likesRepository.postLike(targetMemberId)
    }
}