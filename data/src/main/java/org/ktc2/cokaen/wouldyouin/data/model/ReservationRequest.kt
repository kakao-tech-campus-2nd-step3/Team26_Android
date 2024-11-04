package org.ktc2.cokaen.wouldyouin.data.model
import com.google.gson.annotations.SerializedName

data class ReservationRequest(
    @SerializedName("event_id")
    val eventId: String,
    val price: Int,
    @SerializedName("reservation_count")
    val reservationCount: Int
)