package org.ktc2.cokaen.wouldyouin.feat_profile.repository

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
    suspend fun postLike(targetMemberId: Long): LikeToggleResponse {
        return likesRepository.postLike(targetMemberId)
    }
}