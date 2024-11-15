package org.ktc2.cokaen.wouldyouin.data.model

import com.google.gson.annotations.SerializedName


data class CurationCreateRequestWrapper(
    @SerializedName("curationCreateRequest")
    val curationCreateRequest: CurationCreateRequest,
)

data class CurationCreateRequest(
    val title: String,
    val content: String,
    val curationCards: List<CurationCardRequest>,
    val area: String,
    val hashtags: List<String>,
    val eventIds: List<Long>
)

data class CurationCardRequest(
    val subtitle: String,
    val content: String,
    val imageIds: List<Long>,
)
