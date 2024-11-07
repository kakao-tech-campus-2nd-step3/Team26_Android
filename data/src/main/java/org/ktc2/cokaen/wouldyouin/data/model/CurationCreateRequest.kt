package org.ktc2.cokaen.wouldyouin.data.model

import com.google.gson.annotations.SerializedName


data class CurationCreateRequestWrapper(
    @SerializedName("curationCreateRequest")
    val curationCreateRequest: CurationCreateRequest,

    @SerializedName("curator")
    val curator: MemberIdentifier
)

data class CurationCreateRequest(
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
