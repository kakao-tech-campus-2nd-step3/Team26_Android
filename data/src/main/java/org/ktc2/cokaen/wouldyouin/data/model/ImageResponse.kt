package org.ktc2.cokaen.wouldyouin.data.model

import com.google.gson.annotations.SerializedName

data class ImageResponse(
    @SerializedName("image_id") val imageId: String,        // 이미지 ID
    @SerializedName("url") val url: String,                 // 이미지 URL
    @SerializedName("event_id") val eventId: String? = null, // 행사 ID (nullable)
    @SerializedName("curation_id") val curationId: String? = null // 큐레이션 ID (nullable)
)