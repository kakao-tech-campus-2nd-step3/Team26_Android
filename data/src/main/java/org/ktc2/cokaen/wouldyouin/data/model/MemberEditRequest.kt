package org.ktc2.cokaen.wouldyouin.data.model
import com.google.gson.annotations.SerializedName

data class MemberEditRequest(
    val nickname: String? = null,
    val area: String? = null,
    @SerializedName("phone")  // API 명세의 'phone'과 매핑
    val phoneNumber: String? = null,
    @SerializedName("profile_url")  // snake_case로 전송
    val profileUrl: String? = null
)