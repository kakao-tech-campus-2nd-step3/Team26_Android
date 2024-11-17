package org.ktc2.cokaen.wouldyouin.network.repository

import android.util.Log
import org.ktc2.cokaen.wouldyouin.data.model.CurationCreateRequestWrapper
import org.ktc2.cokaen.wouldyouin.data.model.CurationResponse
import org.ktc2.cokaen.wouldyouin.data.model.CurationSliceResponse
import org.ktc2.cokaen.wouldyouin.data.model.ReviewCreateRequest
import org.ktc2.cokaen.wouldyouin.data.model.ReviewEventSliceResponse
import org.ktc2.cokaen.wouldyouin.data.model.ReviewResponse
import org.ktc2.cokaen.wouldyouin.network.service.ReviewAPIRetrofitService
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReviewRepositoryAPIRetrofitService @Inject constructor(
   private val retrofitService: ReviewAPIRetrofitService
) {
    suspend fun createReview(request: ReviewCreateRequest): ReviewResponse {
        try {
            val response = retrofitService.createReview(request)
            return when {
                response.isSuccessful -> {
                    response.body()?.let { body ->
                        if (body.success) {
                            body.data
                        } else {
                            throw ServerCommonAPIRetrofitRepository.CustomException(
                                body.message ?: "리뷰 생성에 실패했습니다"
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
            Log.e("CreateReview", "Create failed", e)
            throw when (e) {
                is IOException -> ServerCommonAPIRetrofitRepository.CustomException("네트워크 연결을 확인해주세요")
                is HttpException -> ServerCommonAPIRetrofitRepository.CustomException("서버 통신 오류: ${e.code()}")
                else -> e
            }
        }
    }

    suspend fun getPendingReviewList(
        page: Int = 0,
        size: Int = 10,
        lastId: Long = Long.MAX_VALUE
    ): ReviewEventSliceResponse {
        try {
            val response = retrofitService.getReviewEvents(page, size, lastId)
            return when {
                response.isSuccessful -> {
                    response.body()?.let { body ->
                        if (body.success) {
                            body.data
                        } else {
                            throw ServerCommonAPIRetrofitRepository.CustomException(
                                body.message ?: "이벤트 목록 조회에 실패했습니다"
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
            Log.e("PendingEventReview", "Create failed", e)
            throw when (e) {
                is IOException -> ServerCommonAPIRetrofitRepository.CustomException("네트워크 연결을 확인해주세요")
                is HttpException -> ServerCommonAPIRetrofitRepository.CustomException("서버 통신 오류: ${e.code()}")
                else -> e
            }
        }
    }

    suspend fun getReviewList(
        page: Int = 0,
        size: Int = 10,
        lastId: Long = Long.MAX_VALUE
    ): ReviewEventSliceResponse {
        try {
            val response = retrofitService.getReviewEvents(page, size, lastId)
            return when {
                response.isSuccessful -> {
                    response.body()?.let { body ->
                        if (body.success) {
                            body.data
                        } else {
                            throw ServerCommonAPIRetrofitRepository.CustomException(
                                body.message ?: "이벤트 목록 조회에 실패했습니다"
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
            Log.e("WroteReviews", "Create failed", e)
            throw when (e) {
                is IOException -> ServerCommonAPIRetrofitRepository.CustomException("네트워크 연결을 확인해주세요")
                is HttpException -> ServerCommonAPIRetrofitRepository.CustomException("서버 통신 오류: ${e.code()}")
                else -> e
            }
        }
    }
}