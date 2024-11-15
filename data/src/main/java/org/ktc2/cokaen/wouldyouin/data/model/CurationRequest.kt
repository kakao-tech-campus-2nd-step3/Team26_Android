package org.ktc2.cokaen.wouldyouin.data.model

import com.google.gson.annotations.SerializedName
import java.util.UUID

// 추후 수정 필요
data class CurationEditRequestWrapper(
    @SerializedName("curationEditRequest")
    val curationEditRequest: CurationEditRequest
)

data class CurationEditRequest(
    @SerializedName("title")
    val title: String,

    @SerializedName("content")
    val content: String,

    @SerializedName("curationCards")
    val curationCards: List<CurationCardRequest>,

    @SerializedName("area")
    val area: String,

    @SerializedName("hashtags")
    val hashtags: List<String>,

    @SerializedName("eventIds")
    val eventIds: List<Long>,
)

data class Block(
    val id: UUID = UUID.randomUUID(),
    val title: String,
    val images: List<ImageResponse>,
    val body: String
) { fun toCurationCardRequest(): CurationCardRequest {
        return CurationCardRequest(
            subtitle = this.title,
            content = this.body,
            imageIds = this.images.map { it.id }
        )
    }
}



