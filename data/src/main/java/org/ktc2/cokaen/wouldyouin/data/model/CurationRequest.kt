package org.ktc2.cokaen.wouldyouin.data.model

import com.google.gson.annotations.SerializedName

// 추후 수정 필요
data class CurationEditRequestWrapper(
    @SerializedName("curationEditRequest")
    val curationEditRequest: CurationEditRequest,

    @SerializedName("curator")
    val curator: MemberIdentifier
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


data class CurationRequest(
    val title: String,
    val content: String,
    val area: String,
    val hashtags: String,
    val blocks: List<Block>,
    val eventList: List<String>
)

data class CurationRespond(
    val curationId: String,
    val generatedAt: String,
    val title: String,
    val content: String,
    val area: String,
    val hashtags: String,
    val blocks: List<Block>,
    val eventList: List<String>
)

data class Block(
    val title: String,
    val images: List<ImageResponse>,
    val body: String
)



