package org.ktc2.cokaen.wouldyouin.data.model

import com.google.gson.annotations.SerializedName

data class ApiResponseBodyLikeSliceResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("data") val data: LikeSliceResponse,
    @SerializedName("code") val code: String,
    @SerializedName("message") val message: String
)

data class LikeSliceResponse(
    @SerializedName("likes") val likes: List<LikeResponse>,
    @SerializedName("slice") val slice: SliceInfo
)

data class ApiResponseBodyToggleResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("data") val data: LikeToggleResponse,
    @SerializedName("code") val code: String,
    @SerializedName("message") val message: String
)

data class LikeToggleResponse(
    @SerializedName("liked") val liked: Boolean
)