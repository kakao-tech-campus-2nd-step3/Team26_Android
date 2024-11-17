package org.ktc2.cokaen.wouldyouin.data.model

data class ApiResponseBodyCurationResponse(
    val success: Boolean,
    val data: CurationResponse?,
    val code: String,
    val message: String
)

data class CurationResponse(
    val id: Long,
    val curator: CurationCuratorResponse,
    val title: String,
    val content: String,
    val curationCards: List<CurationCardResponse>,
    val area: Area,
    val hashtags: List<String>,
    val eventsInfo: List<CurationEventResponse>,
    val modifiedDate: List<String>,
    val createdTime: List<String>,
    val thumbnailUrl: String
)


data class CurationCuratorResponse(
    val curatorId: Long,
    val nickname: String,
    val email: String,
    val phone: String,
    val profileImageUrl: String,
    val intro: String,
    val likes: Int,
    val hashtags: List<String>
)

data class CurationCardResponse(
    val subtitle: String,
    val imageUrls: List<String>,
    val content: String
)

data class CurationEventResponse(
    val eventId: Long,
    val title: String,
    val location: Location,
    val startTime: List<String>,
    val thumbnailImageUrl: String,
    val hostProfileImageUrl: String,
    val hostNickname: String
)