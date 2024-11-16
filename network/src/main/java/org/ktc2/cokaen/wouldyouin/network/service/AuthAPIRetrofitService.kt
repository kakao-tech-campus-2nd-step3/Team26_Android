package org.ktc2.cokaen.wouldyouin.network.service

import org.ktc2.cokaen.wouldyouin.data.model.ApiResponseBodySocialTokenResponse
import org.ktc2.cokaen.wouldyouin.data.model.ApiResponseBodyTokenResponse
import org.ktc2.cokaen.wouldyouin.data.model.MemberAdditionalInfoRequest
import org.ktc2.cokaen.wouldyouin.data.model.SocialLoginRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthAPIRetrofitService {
    @POST("/api/auth/social/additional-info")
    suspend fun sendAdditionalInfo(
        @Body body: MemberAdditionalInfoRequest
    ): Response<ApiResponseBodyTokenResponse>

    @POST("/api/auth/social/login")
    suspend fun socialLogin(
        @Body socialLoginRequest: SocialLoginRequest
    ): Response<ApiResponseBodySocialTokenResponse>
}