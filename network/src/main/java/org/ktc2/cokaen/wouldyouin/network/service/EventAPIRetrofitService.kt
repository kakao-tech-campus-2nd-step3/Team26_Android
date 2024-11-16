package org.ktc2.cokaen.wouldyouin.network.service

import org.ktc2.cokaen.wouldyouin.data.model.ApiResponseBodyEventResponse
import org.ktc2.cokaen.wouldyouin.data.model.ApiResponseBodyEventSliceResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface EventAPIRetrofitService {

    // 전체 행사 목록 조회 (GET 방식)
    @GET("/api/events")
    suspend fun getEventList(
        @Query("startLatitude") startLatitude: Double,
        @Query("startLongitude") startLongitude: Double,
        @Query("endLatitude") endLatitude: Double,
        @Query("endLongitude") endLongitude: Double,
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("title") title: String? = null,
        @Query("category") category: String? = null,
        @Query("area") area: String? = null,
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 10,
        @Query("lastId") lastId: Long? = null
    ): Response<ApiResponseBodyEventSliceResponse>

    @GET("/api/events/filter")
    suspend fun getEventListDetail(
        @Query("startLatitude") startLatitude: Double? = null,
        @Query("startLongitude") startLongitude: Double? = null,
        @Query("endLatitude") endLatitude: Double? = null,
        @Query("endLongitude") endLongitude: Double? = null,
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("title") title: String? = null,
        @Query("category") category: String? = null,
        @Query("area") area: String? = null,
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 10,
        @Query("lastId") lastId: Long? = null
    ): Response<ApiResponseBodyEventSliceResponse>

    // 단일 행사 상세 조회
    @GET("/api/events/{eventId}")
    suspend fun getEventDetails(
        @Path("eventId") eventId: Long
    ): Response<ApiResponseBodyEventResponse>

    // 주최자별 행사 조회
    @GET("/api/events/hosts/{hostId}")
    suspend fun getEventsByHost(
        @Path("hostId") hostId: Long,
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 10,
        @Query("lastId") lastId: Long? = null
    ): Response<ApiResponseBodyEventSliceResponse>


    // 모든 행사 조회
    @GET("/api/events")
    suspend fun getAllEvents(
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 10,
        @Query("lastId") lastId: Long = Long.MAX_VALUE
    ): Response<ApiResponseBodyEventSliceResponse>
}
