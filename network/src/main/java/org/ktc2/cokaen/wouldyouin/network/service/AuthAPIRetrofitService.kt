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
    fun sendAdditionalInfo(
        @Header("Authorization") token: String,
        @Body body: MemberAdditionalInfoRequest
    ): Call<ApiResponseBodyTokenResponse>

    @GET("api/auth/social/redirect/{accountType}")
    fun socialLoginRedirect(
        @Path("accountType") accountType: String,
        @Query("code") code: String
    ): Call<ApiResponseBodySocialTokenResponse>
}