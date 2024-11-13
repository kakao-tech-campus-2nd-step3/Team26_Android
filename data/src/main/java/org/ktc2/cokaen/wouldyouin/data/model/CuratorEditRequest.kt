package org.ktc2.cokaen.wouldyouin.data.model
import com.google.gson.annotations.SerializedName

// 큐레이터 정보 수정 요청 DTO
data class CuratorEditRequestWrapper(
    val request: CuratorEditRequest
)

data class CuratorEditRequest(
    val nickname: String,
    val phoneNumber: String,
    val profileImageId: Long,
    val area: String,
    val intro: String
)