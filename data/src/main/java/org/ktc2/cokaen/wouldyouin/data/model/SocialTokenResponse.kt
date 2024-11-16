package org.ktc2.cokaen.wouldyouin.data.model

import com.google.gson.annotations.SerializedName

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

// Data Classes for Response
data class ApiResponseBodySocialTokenResponse(
    val success: Boolean,
    val data: SocialTokenResponse?,
    val code: String,
    val message: String
)

data class SocialTokenResponse(
    val isWelcomeMember: Boolean,
    val token: String,
    val memberId: Long,
    val memberType: String // welcome, normal, curator, host, admin
)

