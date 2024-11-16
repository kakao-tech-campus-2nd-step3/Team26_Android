package org.ktc2.cokaen.wouldyouin.data.model
import com.google.gson.annotations.SerializedName

data class ReservationCreateRequestWrapper(
    @SerializedName("reservationRequest")
    val reservationRequest: ReservationRequest
)

data class MemberIdentifier(
    val id: Long,
    val type: String
)

data class ReservationRequest(
    val eventId: Long,
    //val price: Int,
    val quantity: Int
)
