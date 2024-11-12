package org.ktc2.cokaen.wouldyouin.data.model

import com.google.gson.annotations.SerializedName

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
    val area: String, // 예시: "서울"

    @SerializedName("hashTag")
    val hashTag: List<String>,

    @SerializedName("eventIds")
    val eventIds: List<Long>
)

data class Block(
    val title: String,
    val images: List<ImageResponse>,
    val body: String
)



