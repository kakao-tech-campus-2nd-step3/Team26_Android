package org.ktc2.cokaen.wouldyouin.network.repository

import android.content.Context
import android.util.Log
import org.ktc2.cokaen.wouldyouin.core.ToastUtils
import org.ktc2.cokaen.wouldyouin.data.model.ApiResponseBodyEventResponse
import org.ktc2.cokaen.wouldyouin.network.service.EventAPIRetrofitService
import org.ktc2.cokaen.wouldyouin.data.model.ApiResponseBodyEventSliceResponse
import org.ktc2.cokaen.wouldyouin.data.model.EventResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EventAPIRetrofitRepository @Inject constructor(
    private val retrofitService: EventAPIRetrofitService
) {

    // 전체 행사 목록 조회 (GET 방식)
    suspend fun getEventList(
        startLatitude: Double,
        startLongitude: Double,
        endLatitude: Double,
        endLongitude: Double,
        latitude: Double,
        longitude: Double,
        title: String? = null,
        category: String? = null,
        area: String? = null,
        page: Int = 0,
        size: Int = 10,
        lastId: Long? = null,
        context: Context
    ): ApiResponseBodyEventSliceResponse? {
        return try {
            Log.d("EventAPIRetrofitRepository", "Starting to fetch event list with the following parameters:")
            Log.d("EventAPIRetrofitRepository", "startLatitude: $startLatitude, startLongitude: $startLongitude")
            Log.d("EventAPIRetrofitRepository", "endLatitude: $endLatitude, endLongitude: $endLongitude")
            Log.d("EventAPIRetrofitRepository", "latitude: $latitude, longitude: $longitude")

            val response = retrofitService.getEventList(
                startLatitude, startLongitude, endLatitude, endLongitude, latitude, longitude, title, category, area, page, size, lastId
            )

            Log.d("EventAPIRetrofitRepository", "Received response with code: ${response.code()}")

            if (response.isSuccessful) {
                Log.d("EventAPIRetrofitRepository", "Event list fetched successfully. Body: ${response.body()}")
                response.body()
            } else {
                Log.e("EventAPIRetrofitRepository", "Failed to fetch event list. Code: ${response.code()}, Error: ${response.errorBody()?.string()}")
                ToastUtils.showShortToast(context, "행사 목록 조회에 실패했습니다.")
                null
            }
        } catch (e: Exception) {
            Log.e("EventAPIRetrofitRepository", "Exception occurred Event list: ${e.message}", e)
            ToastUtils.showShortToast(context, "오류 발생: ${e.message}")
            null
        }
    }

    /*
    suspend fun searchEvents(
        query: String,
        startLatitude: Double,
        startLongitude: Double,
        endLatitude: Double,
        endLongitude: Double,
        latitude: Double,
        longitude: Double,
        page: Int = 1,
        size: Int = 10,
        context: Context
    ): List<EventResponse>? {
        return try {
            val response = retrofitService.getEventList(
                startLatitude = startLatitude,
                startLongitude = startLongitude,
                endLatitude = endLatitude,
                endLongitude = endLongitude,
                latitude = latitude,
                longitude = longitude,
                title = query,
                page = page,
                size = size
            )
            if (response.isSuccessful && response.body()?.success == true) {
                response.body()?.data?.events
            } else {
                ToastUtils.showShortToast(context, "검색에 실패했습니다.")
                null
            }
        } catch (e: Exception) {
            ToastUtils.showShortToast(context, "오류 발생: ${e.message}")
            null
        }
    }*/

    // 단일 행사 상세 조회
    suspend fun getEventDetails(eventId: Long, context: Context): ApiResponseBodyEventResponse? {
        return try {
            Log.d("EventAPIRetrofitRepository", "Fetching event details for eventId: $eventId")

            val response = retrofitService.getEventDetails(eventId)
            if (response.isSuccessful) {
                Log.d("EventAPIRetrofitRepository", "Event details fetched successfully for eventId: $eventId. Body: ${response.body()}")
                response.body()
            } else {
                Log.e("EventAPIRetrofitRepository", "Failed to fetch event details. Code: ${response.code()}, Error: ${response.errorBody()?.string()}")
                ToastUtils.showShortToast(context, "행사 상세 조회에 실패했습니다.")
                null
            }
        } catch (e: Exception) {
            Log.e("EventAPIRetrofitRepository", "Exception occurred Event Details: ${e.message}", e)
            ToastUtils.showShortToast(context, "오류 발생: ${e.message}")
            null
        }
    }

    //주최자별 행사 조회
    suspend fun getEventsByHost(
        hostId: Long,
        page: Int = 0,
        size: Int = 10,
        lastId: Long? = null,
        context: Context
    ): ApiResponseBodyEventSliceResponse? {
        return try {
            Log.d("EventAPIRetrofitRepository", "Fetching events by host with hostId: $hostId, page: $page, size: $size, lastId: $lastId")

            val response = retrofitService.getEventsByHost(hostId, page, size, lastId)

            Log.d("EventAPIRetrofitRepository", "Received response with code: ${response.code()}")

            if (response.isSuccessful) {
                Log.d("EventAPIRetrofitRepository", "Events by host fetched successfully. Body: ${response.body()}")
                response.body()
            } else {
                Log.e("EventAPIRetrofitRepository", "Failed to fetch events by host. Code: ${response.code()}, Error: ${response.errorBody()?.string()}")
                ToastUtils.showShortToast(context, "주최자별 행사 조회에 실패했습니다.")
                null
            }
        } catch (e: Exception) {
            Log.e("EventAPIRetrofitRepository", "Exception occurred in Events by Host: ${e.message}", e)
            ToastUtils.showShortToast(context, "오류 발생: ${e.message}")
            null
        }
    }
}
