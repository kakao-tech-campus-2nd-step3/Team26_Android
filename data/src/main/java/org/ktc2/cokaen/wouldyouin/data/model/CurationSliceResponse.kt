package org.ktc2.cokaen.wouldyouin.data.model

import com.google.gson.annotations.SerializedName

data class ApiResponseBodyCurationSliceResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("data") val data: CurationSliceResponse,
    @SerializedName("code") val code: String,
    @SerializedName("message") val message: String
)

data class CurationSliceResponse(
    @SerializedName("curations") val curations: List<CurationResponse>,
    @SerializedName("sliceInfo") val sliceInfo: SliceInfo
)