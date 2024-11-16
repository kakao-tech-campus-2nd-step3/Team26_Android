package org.ktc2.cokaen.wouldyouin.network.service

import org.ktc2.cokaen.wouldyouin.data.model.ApiResponseBodySocialTokenResponse
import org.ktc2.cokaen.wouldyouin.data.model.ApiResponseBodyTokenResponse
import org.ktc2.cokaen.wouldyouin.data.model.MemberAdditionalInfoRequest
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface AuthAPIRetrofitService {
    @POST("/api/auth/social/additional-info")
    suspend fun sendAdditionalInfo(
        @Body body: MemberAdditionalInfoRequest
    ): Response<ApiResponseBodyTokenResponse>

    @GET("/api/auth/social/redirect/{accountType}")
    suspend fun socialLoginRedirect(
        @Path("accountType") accountType: String,
        @Query("code") code: String
    ): Response<ApiResponseBodySocialTokenResponse>
}