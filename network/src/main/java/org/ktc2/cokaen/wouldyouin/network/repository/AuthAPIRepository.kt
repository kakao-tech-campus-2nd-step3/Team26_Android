package org.ktc2.cokaen.wouldyouin.network.repository

import android.util.Log
import org.ktc2.cokaen.wouldyouin.data.model.MemberAdditionalInfoRequest
import org.ktc2.cokaen.wouldyouin.data.model.ReservationResponse
import org.ktc2.cokaen.wouldyouin.data.model.SocialTokenResponse
import org.ktc2.cokaen.wouldyouin.data.model.TokenResponse
import org.ktc2.cokaen.wouldyouin.network.service.AuthAPIRetrofitService
import retrofit2.HttpException
import java.io.IOException

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthAPIRepository @Inject constructor(
    private val retrofitService: AuthAPIRetrofitService
) {
    suspend fun socialLoginRedirect(
        accountType: String,
        code: String
    ): SocialTokenResponse {
        try {
            val response = retrofitService.socialLoginRedirect(accountType, code)
            return when {
                response.isSuccessful -> {
                    response.body()?.let { body ->
                        if (body.success) {
                            body.data
                        } else {
                            throw ServerCommonAPIRetrofitRepository.CustomException(
                                body.message ?: "리다이렉트에 실패했습니다"
                            )
                        }
                    } ?: throw ServerCommonAPIRetrofitRepository.CustomException("서버로부터 유효한 응답을 받지 못했습니다")
                }
                else -> {
                    val errorBody = response.errorBody()?.string()
                    throw ServerCommonAPIRetrofitRepository.CustomException("서버 응답 오류: ${response.code()}")
                }
            }
        } catch (e: Exception) {
            Log.e("SocialRedirect", "Fetch failed", e)
            throw when (e) {
                is IOException -> ServerCommonAPIRetrofitRepository.CustomException("네트워크 연결을 확인해주세요")
                is HttpException -> ServerCommonAPIRetrofitRepository.CustomException("서버 통신 오류: ${e.code()}")
                else -> e
            }
        }
    }

    suspend fun sendAdditionalInfo(
        request: MemberAdditionalInfoRequest
    ): TokenResponse {
        try {
            val response = retrofitService.sendAdditionalInfo(request)
            return when {
                response.isSuccessful -> {
                    response.body()?.let { body ->
                        if (body.success) {
                            body.data
                        } else {
                            throw ServerCommonAPIRetrofitRepository.CustomException(
                                body.message ?: "리다이렉트에 실패했습니다"
                            )
                        }
                    } ?: throw ServerCommonAPIRetrofitRepository.CustomException("서버로부터 유효한 응답을 받지 못했습니다")
                }
                else -> {
                    val errorBody = response.errorBody()?.string()
                    throw ServerCommonAPIRetrofitRepository.CustomException("서버 응답 오류: ${response.code()}")
                }
            }
        } catch (e: Exception) {
            Log.e("Additional info", "Fetch failed", e)
            throw when (e) {
                is IOException -> ServerCommonAPIRetrofitRepository.CustomException("네트워크 연결을 확인해주세요")
                is HttpException -> ServerCommonAPIRetrofitRepository.CustomException("서버 통신 오류: ${e.code()}")
                else -> e
            }
        }
    }
}