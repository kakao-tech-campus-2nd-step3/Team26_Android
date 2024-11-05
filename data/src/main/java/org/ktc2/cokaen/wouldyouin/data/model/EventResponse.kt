package org.ktc2.cokaen.wouldyouin.data.model

import com.google.gson.annotations.SerializedName

data class EventResponse(
    @SerializedName("event_id")
    val eventId: String,
    @SerializedName("start_time")
    val startTime: String,
    @SerializedName("end_time")
    val endTime: String,
    val location: Location,
    val area: String,
    val title: String,
    val content: String,
    val host: HostDTO,
    val category: String,
    val price: Int,
    @SerializedName("event_images")
    val eventImages: List<String>,
    @SerializedName("total_seats")
    val totalSeats: String,
    @SerializedName("left_seats")
    val leftSeats: String,
    @SerializedName("is_expired")
    val isExpired: Boolean
) {
    data class HostDTO(
        @SerializedName("member_id")
        val memberId: String,
        @SerializedName("profile_url")
        val profileUrl: String,
        val nickname: String,
        val intro: String,
        val hashtag: List<String>
    )
}