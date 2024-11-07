package org.ktc2.cokaen.wouldyouin.data.model

import com.google.gson.annotations.SerializedName

data class LocalLoginRequest(
    @SerializedName("email")
    val email: String,

    @SerializedName("password")
    val password: String
)