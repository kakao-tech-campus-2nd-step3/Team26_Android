package org.ktc2.cokaen.wouldyouin.data.model

import com.google.gson.annotations.SerializedName

data class ApiResponseBodyVoid(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("data")
    val data: Any?,  // 빈 객체

    @SerializedName("code")
    val code: String,

    @SerializedName("message")
    val message: String
)
