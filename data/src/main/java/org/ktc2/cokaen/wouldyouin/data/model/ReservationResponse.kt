package org.ktc2.cokaen.wouldyouin.data.model
import com.google.gson.annotations.SerializedName

data class ReservationResponse(
    @SerializedName("reservation_id")
    val reservationId: String,
    val member: MemberResponse,
    val event: EventResponse,
    val price: Int,
    @SerializedName("reservation_time")
    val reservationTime: String,
    @SerializedName("reservation_count")
    val reservationCount: Int
)
