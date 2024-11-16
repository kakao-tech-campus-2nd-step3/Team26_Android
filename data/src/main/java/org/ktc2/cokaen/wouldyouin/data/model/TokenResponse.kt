package org.ktc2.cokaen.wouldyouin.data.model

import com.google.gson.annotations.SerializedName

data class ApiResponseBodyTokenResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("data")
    val data: TokenResponse?,

    @SerializedName("code")
    val code: String,

    @SerializedName("message")
    val message: String
)

data class TokenResponse(
    @SerializedName("token")
    val token: String,

    @SerializedName("memberId")
    val memberId : Long,

    @SerializedName("memberType")
    val memberType : MemberType

)
