package org.ktc2.cokaen.wouldyouin.network.service

import org.ktc2.cokaen.wouldyouin.data.model.ApiResponseBodyReviewResponse
import org.ktc2.cokaen.wouldyouin.data.model.ReviewCreateRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ReviewAPIRetrofitService {
    @POST("/api/reviews")
    suspend fun createReview(
        @Body reviewRequest: ReviewCreateRequest
    ): Response<ApiResponseBodyReviewResponse>
}