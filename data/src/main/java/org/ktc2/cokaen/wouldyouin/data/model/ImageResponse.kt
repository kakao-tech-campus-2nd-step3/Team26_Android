package org.ktc2.cokaen.wouldyouin.data.model

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

data class ApiResponseBodyListImageResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("data")
    val data: List<ImageResponse>?,

    @SerializedName("code")
    val code: String?,

    @SerializedName("message")
    val message: String?
)

data class ImageResponse(
    @SerializedName("id")
    val id: Long,

    @SerializedName("url")
    var url: String,

    @SerializedName("createdDate")
    val createdDate: LocalDateTime,

    @SerializedName("size")
    val size: Long,

    @SerializedName("extension")
    val extension: String
)