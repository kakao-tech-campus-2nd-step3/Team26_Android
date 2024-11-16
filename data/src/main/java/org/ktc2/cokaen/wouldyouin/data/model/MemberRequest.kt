package org.ktc2.cokaen.wouldyouin.data.model

import com.google.gson.annotations.SerializedName

data class MemberAdditionalInfoRequest(
    @SerializedName("phone")
    val phone: String,

    @SerializedName("area")
    val area: String,

    @SerializedName("gender")
    val gender: String
)
