package org.ktc2.cokaen.wouldyouin.data.model
import com.google.gson.annotations.SerializedName

// 큐레이터 정보 수정 요청 DTO
data class CuratorEditRequest(
    val nickname: String? = null,
    @SerializedName("phone_number")
    val phoneNumber: String? = null,
    @SerializedName("profile_url")
    val profileUrl: String? = null,
    val area: String? = null,
    val intro: String? = null
)