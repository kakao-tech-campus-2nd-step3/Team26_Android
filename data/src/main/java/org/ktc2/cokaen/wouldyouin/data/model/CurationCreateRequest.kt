package org.ktc2.cokaen.wouldyouin.data.model

import com.google.gson.annotations.SerializedName


data class CurationCreateRequestWrapper(
    @SerializedName("curationCreateRequest")
    val curationCreateRequest: CurationCreateRequest,

    @SerializedName("curator")
    val curator: MemberIdentifier
)

data class CurationCreateRequest(
    val title: String,
    val content: String,
    val curationCards: List<CurationCardRequest>,
    val area: String,
    val hashTag: List<String>,
    val eventIds: List<Long>,
    val curationCardsSizeValid: Boolean = true
)

data class CurationCardRequest(
    val subtitle: String,
    val content: String,
    val imageIds: List<Long>,
    val imageSizeValid: Boolean = true
)

data class CreateCurationRequestBody(
    val curationCreateRequest: CurationCreateRequest,
    val curator: CuratorRequest
)
