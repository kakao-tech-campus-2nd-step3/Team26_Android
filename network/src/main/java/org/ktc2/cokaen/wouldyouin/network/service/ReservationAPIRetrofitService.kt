package org.ktc2.cokaen.wouldyouin.network.service

import okhttp3.ResponseBody
import org.ktc2.cokaen.wouldyouin.data.model.ApiResponseBodyReservationResponse
import org.ktc2.cokaen.wouldyouin.data.model.ApiResponseBodyReservationSliceResponse
import org.ktc2.cokaen.wouldyouin.data.model.ReservationCreateRequestWrapper
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.Response
import org.ktc2.cokaen.wouldyouin.data.model.ReservationRequest
import org.ktc2.cokaen.wouldyouin.data.model.ReservationResponse
import retrofit2.http.Query

interface ReservationAPIRetrofitService {

    // 예매 목록 조회
    @GET("/api/reservations")
    suspend fun getReservationList(
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 10,
        @Query("lastId") lastId: Long = Long.MAX_VALUE
    ): Response<ApiResponseBodyReservationSliceResponse>

    // 예매 생성
    @POST("/api/reservations/test")
    suspend fun createReservation(
        @Body request: ReservationRequest
    ): Response<ApiResponseBodyReservationResponse>

    @GET("/api/reservations/{reservationId}")
    suspend fun getReservation(
        @Path("reservationId") reservationId: Long
    ): Response<ApiResponseBodyReservationResponse>

    @DELETE("/api/reservations/{reservationId}")
    suspend fun deleteReservation(
        @Path("reservationId") reservationId: Long
    ): Response<ResponseBody>


//    // 특정 예매 조회
//    @GET("api/reservations/{reservationId}")
//    suspend fun getReservationDetails(
//        @Path("reservationId") reservationId: String
//    ): Response<ReservationResponse>

//    // 예매 취소
//    @DELETE("api/reservations/{reservationId}")
//    suspend fun cancelReservation(
//        @Path("reservationId") reservationId: String
//    ): Response<Unit>
}