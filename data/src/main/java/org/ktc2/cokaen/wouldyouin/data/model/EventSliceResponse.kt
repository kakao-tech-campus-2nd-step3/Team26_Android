package org.ktc2.cokaen.wouldyouin.data.model

import com.google.gson.annotations.SerializedName

data class ApiResponseBodyEventSliceResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("data") val data: EventSliceResponse,
    @SerializedName("code") val code: String,
    @SerializedName("message") val message: String
)

data class EventSliceResponse(
    @SerializedName("events") val events: List<EventResponse>,
    @SerializedName("slice") val slice: SliceInfo
)