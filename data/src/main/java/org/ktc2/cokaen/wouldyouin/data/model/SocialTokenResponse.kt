package org.ktc2.cokaen.wouldyouin.data.model

import com.google.gson.annotations.SerializedName

data class ApiResponseBodySocialTokenResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("data")
    val data: SocialTokenResponse,

    @SerializedName("code")
    val code: String,

    @SerializedName("message")
    val message: String
)

data class SocialTokenResponse(
    val isWelcomeMember: Boolean,
    val token: String
)
