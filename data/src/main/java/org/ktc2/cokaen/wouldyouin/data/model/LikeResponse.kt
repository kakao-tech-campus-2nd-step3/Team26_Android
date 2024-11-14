package org.ktc2.cokaen.wouldyouin.data.model

import com.google.gson.annotations.SerializedName

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