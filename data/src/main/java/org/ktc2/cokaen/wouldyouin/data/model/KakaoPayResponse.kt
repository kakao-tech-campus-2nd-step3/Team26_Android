package org.ktc2.cokaen.wouldyouin.data.model

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

data class ApiResponseBodyKakaoPayResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("data")
    val data: KakaoPayResponse?,

    @SerializedName("code")
    val code: String?,

    @SerializedName("message")
    val message: String?
)

data class KakaoPayResponse(
    @SerializedName("tid")
    val tid: String,

    @SerializedName("next_redirect_app_url")
    val nextRedirectAppUrl: String,

    @SerializedName("next_redirect_mobile_url")
    val nextRedirectMobileUrl: String,

    @SerializedName("next_redirect_pc_url")
    val nextRedirectPcUrl: String,

    @SerializedName("android_app_scheme")
    val androidAppScheme: String,

    @SerializedName("ios_app_scheme")
    val iosAppScheme: String,

    @SerializedName("created_at")
    val createdAt: String
)
