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
        @Query("page") page: Int? = null,
        @Query("size") size: Int? = null,
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
        @Query("page") page: Int? = null,
        @Query("size") size: Int? = null,
        @Query("lastId") lastId: Long? = null
    ): Response<ApiResponseBodyEventSliceResponse>
}


/*
package org.ktc2.cokaen.wouldyouin.network.service

import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.PUT
import retrofit2.Response
import org.ktc2.cokaen.wouldyouin.data.model.EventRequest
import org.ktc2.cokaen.wouldyouin.data.model.EventResponse

interface EventAPIRetrofitService {

    // 전체 행사 목록 조회
    @GET("api/events")
    suspend fun getEventList(): Response<List<EventResponse>>

    // 주최자 별 행사 목록 조회
    @GET("api/events")
    suspend fun getEventsByHost(
        @Query("memberId") memberId: String
    ): Response<List<EventResponse>>

    // 단일 행사 상세 조회
    @GET("api/events/{eventId}")
    suspend fun getEventDetails(
        @Path("eventId") eventId: String
    ): Response<EventResponse>

    // 행사 생성
    @POST("api/events")
    suspend fun createEvent(
        @Body eventRequest: EventRequest
    ): Response<EventResponse>

    // 행사 삭제
    @DELETE("api/events/{eventId}")
    suspend fun deleteEvent(
        @Path("eventId") eventId: String
    ): Response<Unit>

    // 행사 수정
    @PUT("api/events/{eventId}")
    suspend fun updateEvent(
        @Path("eventId") eventId: String,
        @Body eventRequest: EventRequest
    ): Response<EventResponse>
}*/
