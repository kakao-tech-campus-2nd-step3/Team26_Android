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
    val modifiedDate: String,
    val createdTime: String
)


data class CurationCuratorResponse(
    val nickname: String,
    val email: String,
    val phone: String,
    val profileImageUrl: String,
    val intro: String,
    val likes: Int,
    val hashtags: List<String>
)

data class CurationCardResponse(
    val title: String,
    val imageUrls: List<String>,
    val body: String
)

data class CurationEventResponse(
    val id: Long,
    val title: String,
    val location: Location,
    val startTime: String,
    val thumbnailImageUrl: String,
    val hostProfileImageUrl: String,
    val hostNickname: String
)


data class LocalCurationCard(
    // 임시 -> 추후 수정 필요!!!
    val title: String,
    val images: List<ImageResponse>,
    val body: String
)