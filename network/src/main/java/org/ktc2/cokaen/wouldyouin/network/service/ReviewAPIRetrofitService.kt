package org.ktc2.cokaen.wouldyouin.network.service

import org.ktc2.cokaen.wouldyouin.data.model.ApiResponseBodyReviewEventSliceResponse
import org.ktc2.cokaen.wouldyouin.data.model.ApiResponseBodyReviewResponse
import org.ktc2.cokaen.wouldyouin.data.model.ReviewCreateRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ReviewAPIRetrofitService {
    @POST("/api/reviews")
    suspend fun createReview(
        @Body reviewRequest: ReviewCreateRequest
    ): Response<ApiResponseBodyReviewResponse>

    @GET("/api/reviews")
    suspend fun getReviewEvents(
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 10,
        @Query("lastId") lastId: Long = Long.MAX_VALUE
    ): Response<ApiResponseBodyReviewEventSliceResponse>

    @GET("/api/reviews")
    suspend fun getReviews(
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 10,
        @Query("lastId") lastId: Long = Long.MAX_VALUE
    ): Response<ApiResponseBodyReviewEventSliceResponse>
}