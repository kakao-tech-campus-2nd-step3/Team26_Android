package org.ktc2.cokaen.wouldyouin.data.model

import java.time.LocalDateTime

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
    val hashTag: List<String>,
    val eventsInfo: List<CurationEventResponse>,
    val modifiedDate: LocalDateTime,
    val createdTime: LocalDateTime,
    val thumbnailUrl: String
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
    val eventId: Long,
    val title: String,
    val location: Location,
    val startTime: LocalDateTime,
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