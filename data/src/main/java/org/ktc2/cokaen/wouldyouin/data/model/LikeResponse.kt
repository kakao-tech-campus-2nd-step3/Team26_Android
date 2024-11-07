package org.ktc2.cokaen.wouldyouin.data.model

import com.google.gson.annotations.SerializedName

data class ApiResponseBodyListLikeResponse(
    @SerializedName("success")
    val success: Boolean,  // 요청 성공 여부

    @SerializedName("data")
    val data: List<LikeResponse>,  // 좋아요한 멤버 목록

    @SerializedName("code")
    val code: String,  // 응답 코드

    @SerializedName("message")
    val message: String  // 메시지
)

data class LikeResponse(
    @SerializedName("memberId")
    val memberId: Long,  // 좋아요한 멤버의 ID

    @SerializedName("nickname")
    val nickname: String,  // 멤버의 닉네임

    @SerializedName("intro")
    val intro: String,  // 멤버의 소개

    @SerializedName("hashtags")
    val hashtags: List<String>,  // 멤버의 해시태그 목록

    @SerializedName("profileImageUrl")
    val profileImageUrl: String  // 멤버의 프로필 이미지 URL
)
