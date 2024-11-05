package org.ktc2.cokaen.wouldyouin.network.Repository

import android.content.Context
import org.ktc2.cokaen.wouldyouin.core.ToastUtils
import org.ktc2.cokaen.wouldyouin.network.Service.EventAPIRetrofitService
import org.ktc2.cokaen.wouldyouin.data.model.EventRequest
import org.ktc2.cokaen.wouldyouin.data.model.EventResponse
import javax.inject.Inject

open class EventAPIRetrofitRepository @Inject constructor(
    private val retrofitService: EventAPIRetrofitService
) {
    // 전체 행사 목록 조회
    open suspend fun getEventList(context: Context): List<EventResponse>? {
        return try {
            val response = retrofitService.getEventList()
            if (response.isSuccessful) {
                response.body()
            } else {
                ToastUtils.showShortToast(context, "행사 목록 조회에 실패했습니다.")
                null
            }
        } catch (e: Exception) {
            ToastUtils.showShortToast(context, "오류 발생: ${e.message}")
            null
        }
    }

    // 주최자 별 행사 목록 조회
    open suspend fun getEventsByHost(memberId: String, context: Context): List<EventResponse>? {
        return try {
            val response = retrofitService.getEventsByHost(memberId)
            if (response.isSuccessful) {
                response.body()
            } else {
                ToastUtils.showShortToast(context, "주최자별 행사 목록 조회에 실패했습니다.")
                null
            }
        } catch (e: Exception) {
            ToastUtils.showShortToast(context, "오류 발생: ${e.message}")
            null
        }
    }

    // 단일 행사 상세 조회
    open suspend fun getEventDetails(eventId: String, context: Context): EventResponse? {
        return try {
            val response = retrofitService.getEventDetails(eventId)
            if (response.isSuccessful) {
                response.body()
            } else {
                ToastUtils.showShortToast(context, "행사 상세 조회에 실패했습니다.")
                null
            }
        } catch (e: Exception) {
            ToastUtils.showShortToast(context, "오류 발생: ${e.message}")
            null
        }
    }

    // 행사 생성
    open suspend fun createEvent(eventRequest: EventRequest, context: Context): EventResponse? {
        return try {
            val response = retrofitService.createEvent(eventRequest)
            if (response.isSuccessful) {
                response.body()
            } else {
                ToastUtils.showShortToast(context, "행사 생성에 실패했습니다.")
                null
            }
        } catch (e: Exception) {
            ToastUtils.showShortToast(context, "오류 발생: ${e.message}")
            null
        }
    }

    // 행사 삭제
    open suspend fun deleteEvent(eventId: String, context: Context): Boolean {
        return try {
            val response = retrofitService.deleteEvent(eventId)
            if (response.isSuccessful) {
                true
            } else {
                ToastUtils.showShortToast(context, "행사 삭제에 실패했습니다.")
                false
            }
        } catch (e: Exception) {
            ToastUtils.showShortToast(context, "오류 발생: ${e.message}")
            false
        }
    }

    // 행사 수정
    open suspend fun updateEvent(eventId: String, eventRequest: EventRequest, context: Context): EventResponse? {
        return try {
            val response = retrofitService.updateEvent(eventId, eventRequest)
            if (response.isSuccessful) {
                response.body()
            } else {
                ToastUtils.showShortToast(context, "행사 수정에 실패했습니다.")
                null
            }
        } catch (e: Exception) {
            ToastUtils.showShortToast(context, "오류 발생: ${e.message}")
            null
        }
    }
}