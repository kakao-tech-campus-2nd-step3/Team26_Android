package org.ktc2.cokaen.wouldyouin.network.Service

import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.PUT
import retrofit2.Response
import org.ktc2.cokaen.wouldyouin.data.model.ReservationRequest
import org.ktc2.cokaen.wouldyouin.data.model.ReservationResponse

interface ReservationAPIRetrofitService {

    // 예매 목록 조회
    @GET("api/reservations")
    suspend fun getReservationList(): Response<List<ReservationResponse>>

    // 예매 생성
    @POST("api/reservations")
    suspend fun createReservation(
        @Body reservationRequest: ReservationRequest
    ): Response<ReservationResponse>

    // 특정 예매 조회
    @GET("api/reservations/{reservationId}")
    suspend fun getReservationDetails(
        @Path("reservationId") reservationId: String
    ): Response<ReservationResponse>

    // 예매 취소
    @DELETE("api/reservations/{reservationId}")
    suspend fun cancelReservation(
        @Path("reservationId") reservationId: String
    ): Response<Unit>
}