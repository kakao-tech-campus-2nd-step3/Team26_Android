package org.ktc2.cokaen.wouldyouin.network.repository

import android.content.Context
import android.util.Log
import org.ktc2.cokaen.wouldyouin.core.ToastUtils
import org.ktc2.cokaen.wouldyouin.data.model.ApiResponseBodyReservationResponse
import org.ktc2.cokaen.wouldyouin.data.model.CurationResponse
import org.ktc2.cokaen.wouldyouin.data.model.CurationSliceResponse
import org.ktc2.cokaen.wouldyouin.data.model.ReservationCreateRequestWrapper
import org.ktc2.cokaen.wouldyouin.network.service.ReservationAPIRetrofitService
import org.ktc2.cokaen.wouldyouin.data.model.ReservationRequest
import org.ktc2.cokaen.wouldyouin.data.model.ReservationResponse
import org.ktc2.cokaen.wouldyouin.data.model.ReservationSliceResponse
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class ReservationAPIRetrofitRepository @Inject constructor(
    private val retrofitService: ReservationAPIRetrofitService
) {
    // 예매 목록 조회
    suspend fun getReservationList(
        page: Int = 0,
        size: Int = 10,
        lastId: Long = Long.MAX_VALUE
    ): ReservationSliceResponse {
        try {
            val response = retrofitService.getReservationList(page, size, lastId)
            return when {
                response.isSuccessful -> {
                    response.body()?.let { body ->
                        if (body.success) {
                            body.data
                        } else {
                            throw ServerCommonAPIRetrofitRepository.CustomException(
                                body.message ?: "예매 목록 조회에 실패했습니다"
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
            Log.e("GetReservationList", "Create failed", e)
            throw when (e) {
                is IOException -> ServerCommonAPIRetrofitRepository.CustomException("네트워크 연결을 확인해주세요")
                is HttpException -> ServerCommonAPIRetrofitRepository.CustomException("서버 통신 오류: ${e.code()}")
                else -> e
            }
        }
    }

    // 예매 생성
    suspend fun createReservation(request: ReservationRequest): ApiResponseBodyReservationResponse? {
        return try {
            val response = retrofitService.createReservation(request)
            if (response.isSuccessful) {
                response.body()
            } else {
                Log.e("ReservationRepository", "Error creating reservation: ${response.errorBody()?.string()}")
                null
            }
        } catch (e: Exception) {
            Log.e("ReservationRepository", "Exception creating reservation", e)
            null
        }
    }

    suspend fun getReservationDetails(reservationId: Long): ReservationResponse {
        try {
            val response = retrofitService.getReservation(reservationId)
            return when {
                response.isSuccessful -> {
                    response.body()?.let { body ->
                        if (body.success) {
                            body.data
                        } else {
                            throw ServerCommonAPIRetrofitRepository.CustomException(
                                body.message ?: "예매 상세 정보 조회에 실패했습니다"
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
            Log.e("GetReservationDetail", "Fetch failed", e)
            throw when (e) {
                is IOException -> ServerCommonAPIRetrofitRepository.CustomException("네트워크 연결을 확인해주세요")
                is HttpException -> ServerCommonAPIRetrofitRepository.CustomException("서버 통신 오류: ${e.code()}")
                else -> e
            }
        }
    }

    suspend fun deleteReservation(reservationId: Long): Boolean {
        return try {
            val response = retrofitService.deleteReservation(reservationId)
            if (response.isSuccessful) {
                true
            } else {
                throw Exception("예매 취소에 실패했습니다. 오류 코드: ${response.code()}")
            }
        } catch (e: Exception) {
            when (e) {
                is IOException -> throw Exception("네트워크 연결을 확인해주세요")
                is HttpException -> throw Exception("서버 통신 중 오류가 발생했습니다")
                else -> throw e
            }
        }
    }
}

//// 예매 생성
//open suspend fun createReservation(
//    reservationRequest: ReservationRequest,
//    context: Context
//): ReservationResponse? {
//    return try {
//        val response = retrofitService.createReservation(reservationRequest)
//        if (response.isSuccessful) {
//            response.body()
//        } else {
//            ToastUtils.showShortToast(context, "예매 생성에 실패했습니다.")
//            null
//        }
//    } catch (e: Exception) {
//        ToastUtils.showShortToast(context, "오류 발생: ${e.message}")
//        null
//    }
//}
//
//// 특정 예매 조회
//open suspend fun getReservationDetails(
//    reservationId: String,
//    context: Context
//): ReservationResponse? {
//    return try {
//        val response = retrofitService.getReservationDetails(reservationId)
//        if (response.isSuccessful) {
//            response.body()
//        } else {
//            ToastUtils.showShortToast(context, "예매 조회에 실패했습니다.")
//            null
//        }
//    } catch (e: Exception) {
//        ToastUtils.showShortToast(context, "오류 발생: ${e.message}")
//        null
//    }
//}
//
//// 예매 취소
//open suspend fun cancelReservation(
//    reservationId: String,
//    context: Context
//): Boolean {
//    return try {
//        val response = retrofitService.cancelReservation(reservationId)
//        if (response.isSuccessful) {
//            true
//        } else {
//            ToastUtils.showShortToast(context, "예매 취소에 실패했습니다.")
//            false
//        }
//    } catch (e: Exception) {
//        ToastUtils.showShortToast(context, "오류 발생: ${e.message}")
//        false
//    }
//}