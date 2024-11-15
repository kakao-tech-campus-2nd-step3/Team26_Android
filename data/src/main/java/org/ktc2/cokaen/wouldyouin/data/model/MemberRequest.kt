package org.ktc2.cokaen.wouldyouin.data.model

import com.google.gson.annotations.SerializedName

data class MemberAdditionalInfoRequest(
    @SerializedName("phone")
    val phone: String,

    @SerializedName("area")
    val area: Area, // Enum을 통해 지역 값을 제한할 수 있습니다.

    @SerializedName("gender")
    val gender: String
)
