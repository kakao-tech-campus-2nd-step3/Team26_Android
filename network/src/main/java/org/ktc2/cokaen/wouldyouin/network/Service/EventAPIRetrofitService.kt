package org.ktc2.cokaen.wouldyouin.network.Service

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
}