package org.ktc2.cokaen.wouldyouin.network.service

import org.ktc2.cokaen.wouldyouin.data.model.ApiResponseBodyLikeSliceResponse
import org.ktc2.cokaen.wouldyouin.data.model.ApiResponseBodyToggleResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface LikesAPIRetrofitService {
    @POST("/api/likes/{targetMemberId}")
    suspend fun postLike(
        @Path("targetMemberId") targetMemberId: Long,
        @Query("type") type: String
    ): Response<ApiResponseBodyToggleResponse>

    @GET("/api/likes")
    suspend fun getLikes(
        @Query("type") type: String,
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 10,
        @Query("lastId") lastId: Long = Long.MAX_VALUE
    ): Response<ApiResponseBodyLikeSliceResponse>
}
