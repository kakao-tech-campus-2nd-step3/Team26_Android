package org.ktc2.cokaen.wouldyouin.network.Repository

import android.content.Context
import org.ktc2.cokaen.wouldyouin.core.ToastUtils
import org.ktc2.cokaen.wouldyouin.network.Service.ReservationAPIRetrofitService
import org.ktc2.cokaen.wouldyouin.data.model.ReservationRequest
import org.ktc2.cokaen.wouldyouin.data.model.ReservationResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class ReservationAPIRetrofitRepository @Inject constructor(
    private val retrofitService: ReservationAPIRetrofitService
) {
    // 예매 목록 조회
    open suspend fun getReservationList(context: Context): List<ReservationResponse>? {
        return try {
            val response = retrofitService.getReservationList()
            if (response.isSuccessful) {
                response.body()
            } else {
                ToastUtils.showShortToast(context, "예매 목록 조회에 실패했습니다.")
                null
            }
        } catch (e: Exception) {
            ToastUtils.showShortToast(context, "오류 발생: ${e.message}")
            null
        }
    }

    // 예매 생성
    open suspend fun createReservation(
        reservationRequest: ReservationRequest,
        context: Context
    ): ReservationResponse? {
        return try {
            val response = retrofitService.createReservation(reservationRequest)
            if (response.isSuccessful) {
                response.body()
            } else {
                ToastUtils.showShortToast(context, "예매 생성에 실패했습니다.")
                null
            }
        } catch (e: Exception) {
            ToastUtils.showShortToast(context, "오류 발생: ${e.message}")
            null
        }
    }

    // 특정 예매 조회
    open suspend fun getReservationDetails(
        reservationId: String,
        context: Context
    ): ReservationResponse? {
        return try {
            val response = retrofitService.getReservationDetails(reservationId)
            if (response.isSuccessful) {
                response.body()
            } else {
                ToastUtils.showShortToast(context, "예매 조회에 실패했습니다.")
                null
            }
        } catch (e: Exception) {
            ToastUtils.showShortToast(context, "오류 발생: ${e.message}")
            null
        }
    }

    // 예매 취소
    open suspend fun cancelReservation(
        reservationId: String,
        context: Context
    ): Boolean {
        return try {
            val response = retrofitService.cancelReservation(reservationId)
            if (response.isSuccessful) {
                true
            } else {
                ToastUtils.showShortToast(context, "예매 취소에 실패했습니다.")
                false
            }
        } catch (e: Exception) {
            ToastUtils.showShortToast(context, "오류 발생: ${e.message}")
            false
        }
    }
}