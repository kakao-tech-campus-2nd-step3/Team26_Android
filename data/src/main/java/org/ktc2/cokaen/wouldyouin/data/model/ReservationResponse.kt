package org.ktc2.cokaen.wouldyouin.data.model
import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime
import java.util.Date

data class ApiResponseBodyReservationSliceResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("data")
    val data: ReservationSliceResponse,

    @SerializedName("code")
    val code: String,

    @SerializedName("message")
    val message: String
)

data class ReservationSliceResponse(
    @SerializedName("reservations")
    val reservations: List<ReservationResponse>,

    @SerializedName("sliceInfo")
    val sliceInfo: SliceInfo
)

data class ReservationResponse(
    @SerializedName("id")
    val id: Long,

    @SerializedName("member")
    val member: ReservationMemberResponse,

    @SerializedName("event")
    val event: ReservationEventResponse,

    @SerializedName("price")
    val price: Int,

    @SerializedName("quantity")
    val quantity: Int,

    @SerializedName("reservationDate")
    val reservationDate: LocalDateTime
)

data class ReservationMemberResponse(
    @SerializedName("id")
    val id: Long,

    @SerializedName("email")
    val email: String,

    @SerializedName("nickname")
    val nickname: String,

    @SerializedName("phone")
    val phone: String,

    @SerializedName("gender")
    val gender: String
)

data class ReservationEventResponse(
    @SerializedName("id")
    val id: Long,

    @SerializedName("startTime") //TODO 백엔드측 전달
    val startTime: LocalDateTime,

    @SerializedName("title")
    val title: String,

    @SerializedName("price")
    val price: Int,

    @SerializedName("Location")
    val location: Location,

    @SerializedName("thumbnailUrl")
    val imageUrl: String
)

