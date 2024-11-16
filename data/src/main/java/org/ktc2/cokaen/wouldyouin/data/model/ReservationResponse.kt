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
    val reservationDate: List<String>
)

data class ReservationMemberResponse(
    @SerializedName("memberId")
    val memberId: Long,

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
    @SerializedName("eventId")
    val eventId: Long,

    @SerializedName("startTime")
    val startTime: List<String>,

    @SerializedName("title")
    val title: String,

    @SerializedName("price")
    val price: Int,

    @SerializedName("Location")
    val location: Location,

    @SerializedName("thumbnailUrl")
    val thumbnailUrl: String
)

data class ApiResponseBodyReservationResponse(
    val success: Boolean,
    val data: ReservationResponse?,
    val code: String,
    val message: String
)

