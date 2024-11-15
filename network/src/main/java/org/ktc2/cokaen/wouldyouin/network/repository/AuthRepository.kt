package org.ktc2.cokaen.wouldyouin.network.repository

import com.kakao.sdk.auth.AuthApiClient
import org.ktc2.cokaen.wouldyouin.data.model.ApiResponseBodySocialTokenResponse
import org.ktc2.cokaen.wouldyouin.data.model.ApiResponseBodyTokenResponse
import org.ktc2.cokaen.wouldyouin.data.model.MemberAdditionalInfoRequest
import org.ktc2.cokaen.wouldyouin.network.service.AuthAPIRetrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

import retrofit2.converter.gson.GsonConverterFactory

class AuthRepository {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://wouldyouin.store")  // 실제 서버 주소로 변경
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val authService = retrofit.create(AuthAPIRetrofitService::class.java)

    fun sendAdditionalInfo(
        token: String,
        additionalInfo: MemberAdditionalInfoRequest,
        onSuccess: (ApiResponseBodyTokenResponse) -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
        authService.sendAdditionalInfo(token, additionalInfo)
            .enqueue(object : Callback<ApiResponseBodyTokenResponse> {
                override fun onResponse(
                    call: Call<ApiResponseBodyTokenResponse>,
                    response: Response<ApiResponseBodyTokenResponse>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.let { onSuccess(it) }
                    } else {
                        onFailure(Throwable("서버 응답 오류: ${response.code()}"))
                    }
                }

                override fun onFailure(call: Call<ApiResponseBodyTokenResponse>, t: Throwable) {
                    onFailure(t)
                }
            })
    }

    fun fetchToken(
        accountType: String,
        code: String,
        onSuccess: (token: String, isWelcome: Boolean) -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
        authService.socialLoginRedirect(accountType, code)
            .enqueue(object : Callback<ApiResponseBodySocialTokenResponse> {
                override fun onResponse(
                    call: Call<ApiResponseBodySocialTokenResponse>,
                    response: Response<ApiResponseBodySocialTokenResponse>
                ) {
                    if (response.isSuccessful) {
                        val body = response.body()
                        val token = body?.data?.token
                        val isWelcome = body?.data?.isWelcomeMember ?: false

                        if (token != null) {
                            onSuccess(token, isWelcome)
                        } else {
                            onFailure(Throwable("토큰을 가져올 수 없습니다."))
                        }
                    } else {
                        onFailure(Throwable("서버 응답 오류: ${response.code()}"))
                    }
                }

                override fun onFailure(
                    call: Call<ApiResponseBodySocialTokenResponse>,
                    t: Throwable
                ) {
                    onFailure(t)
                }
            })
    }
}