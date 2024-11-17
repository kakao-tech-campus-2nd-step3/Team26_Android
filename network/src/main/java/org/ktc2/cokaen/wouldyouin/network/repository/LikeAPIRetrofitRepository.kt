package org.ktc2.cokaen.wouldyouin.network.repository

import android.util.Log
import org.ktc2.cokaen.wouldyouin.data.model.LikeSliceResponse
import org.ktc2.cokaen.wouldyouin.data.model.LikeToggleResponse
import org.ktc2.cokaen.wouldyouin.data.model.MemberType
import org.ktc2.cokaen.wouldyouin.network.service.LikesAPIRetrofitService
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LikesAPIRetrofitRepository @Inject constructor(
    private val retrofitService: LikesAPIRetrofitService
) {
    suspend fun postLike(targetMemberId: Long, type: MemberType): LikeToggleResponse {
        try {
            val response = retrofitService.postLike(targetMemberId, type.name)
            return when {
                response.isSuccessful -> {
                    response.body()?.let { body ->
                        if (body.success) {
                            body.data
                        } else {
                            throw ServerCommonAPIRetrofitRepository.CustomException(
                                body.message ?: "취소/추가 변경에 실패했습니다"
                            )
                        }
                    }
                        ?: throw ServerCommonAPIRetrofitRepository.CustomException("서버로부터 유효한 응답을 받지 못했습니다")
                }

                else -> {
                    val errorBody = response.errorBody()?.string()
                    Log.d("message", response.message())
                    throw ServerCommonAPIRetrofitRepository.CustomException("서버 응답 오류: ${response.code()}")
                }
            }
        } catch (e: Exception) {
            Log.e("PostLike", "Post failed", e)
            throw when (e) {
                is IOException -> ServerCommonAPIRetrofitRepository.CustomException("네트워크 연결을 확인해주세요")
                is HttpException -> ServerCommonAPIRetrofitRepository.CustomException("서버 통신 오류: ${e.code()}")
                else -> e
            }
        }
    }

    suspend fun getLikes(
        type: String,
        page: Int = 0,
        size: Int = 10,
        lastId: Long = Long.MAX_VALUE
    ): LikeSliceResponse {
        try {
            val response = retrofitService.getLikes(type, page, size, lastId)
            return when {
                response.isSuccessful -> {
                    response.body()?.let { body ->
                        if (body.success) {
                            body.data
                        } else {
                            throw ServerCommonAPIRetrofitRepository.CustomException(
                                body.message ?: "큐레이션 목록 조회에 실패했습니다"
                            )
                        }
                    }
                        ?: throw ServerCommonAPIRetrofitRepository.CustomException("서버로부터 유효한 응답을 받지 못했습니다")
                }

                else -> {
                    val errorBody = response.errorBody()?.string()
                    throw ServerCommonAPIRetrofitRepository.CustomException("서버 응답 오류: ${response.code()}")
                }
            }
        } catch (e: Exception) {
            Log.e("GetLikeList", "Create failed", e)
            throw when (e) {
                is IOException -> ServerCommonAPIRetrofitRepository.CustomException("네트워크 연결을 확인해주세요")
                is HttpException -> ServerCommonAPIRetrofitRepository.CustomException("서버 통신 오류: ${e.code()}")
                else -> e
            }
        }
    }
}