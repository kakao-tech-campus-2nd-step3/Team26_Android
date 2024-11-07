package org.ktc2.cokaen.wouldyouin.data.model

import com.google.gson.annotations.SerializedName

class CurationSliceResponse {
    data class ApiResponseBodyCurationSliceResponse(
        @SerializedName("success") val success: Boolean,
        @SerializedName("data") val data: CurationSliceResponse,
        @SerializedName("code") val code: String,
        @SerializedName("message") val message: String
    )

    data class CurationSliceResponse(
        @SerializedName("events") val events: List<CurationResponse>,
        @SerializedName("slice") val slice: SliceInfo
    )
}