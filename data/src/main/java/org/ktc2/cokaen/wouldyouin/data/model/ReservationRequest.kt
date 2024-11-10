package org.ktc2.cokaen.wouldyouin.data.model
import com.google.gson.annotations.SerializedName

data class ReservationRequest(
    val eventId: Long,
    val price: Int,
    val quantity: Int
)
