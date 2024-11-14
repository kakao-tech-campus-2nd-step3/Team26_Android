package org.ktc2.cokaen.wouldyouin.feat_profile.repository

import org.ktc2.cokaen.wouldyouin.data.model.ReviewCreateRequest
import org.ktc2.cokaen.wouldyouin.data.model.ReviewResponse
import org.ktc2.cokaen.wouldyouin.network.repository.CurationAPIRetrofitRepository
import org.ktc2.cokaen.wouldyouin.network.repository.EventAPIRetrofitRepository
import org.ktc2.cokaen.wouldyouin.network.repository.ReviewRepositoryAPIRetrofitService
import org.ktc2.cokaen.wouldyouin.network.repository.ServerCommonAPIRetrofitRepository
import org.ktc2.cokaen.wouldyouin.network.service.ReviewAPIRetrofitService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EventReviewRepository @Inject constructor(
    private val repository: ReviewRepositoryAPIRetrofitService
) {
    suspend fun submitReview(request: ReviewCreateRequest): ReviewResponse {
        return repository.createReview(request)
    }
}