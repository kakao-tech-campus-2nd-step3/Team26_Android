package org.ktc2.cokaen.wouldyouin.data.model

import com.google.gson.annotations.SerializedName

data class ApiResponseBodyEventResponse(
    val success: Boolean,
    val data: EventResponse?,
    val code: String,
    val message: String
)

data class EventResponse(
    val id: Long,
    val title: String,
    val content: String,
    val host: EventHostResponse,
    val area: Area,
    val location: Location,
    val startTime: String,
    val endTime: String,
    val price: Int,
    val totalSeat: Int,
    val leftSeat: Int,
    val category: Category,
    val expired: Boolean
)

data class EventHostResponse(
    val nickname: String,
    val email: String,
    val phone: String,
    val profileImageUrl: String,
    val intro: String,
    val likes: Int,
    val hashtags: List<String>
)

enum class Category {
    //MUSIC, ART, SPORTS, EDUCATION, TECHNOLOGY, LITERATURE, FILM, OTHER // 필요시 추가
    전체, 밴드, 연극, 뮤지컬, 원데이클래스, 전시회, 공예, 축제
}

//
//data class EventResponse(
//    @SerializedName("event_id")
//    val eventId: String,
//    @SerializedName("start_time")
//    val startTime: String,
//    @SerializedName("end_time")
//    val endTime: String,
//    val location: Location,
//    val area: String,
//    val title: String,
//    val content: String,
//    val host: HostDTO,
//    val category: String,
//    val price: Int,
//    @SerializedName("event_images")
//    val eventImages: List<String>,
//    @SerializedName("total_seats")
//    val totalSeats: String,
//    @SerializedName("left_seats")
//    val leftSeats: String,
//    @SerializedName("is_expired")
//    val isExpired: Boolean
//) {
//    data class HostDTO(
//        @SerializedName("member_id")
//        val memberId: String,
//        @SerializedName("profile_url")
//        val profileUrl: String,
//        val nickname: String,
//        val intro: String,
//        val hashtag: List<String>
//    )
//}