package org.ktc2.cokaen.wouldyouin.data.model
import com.google.gson.annotations.SerializedName

data class MemberRequestWrapper(
    @SerializedName("editRequest") val editRequest: MemberEditRequest
)

data class MemberEditRequest(
    @SerializedName("nickname") val nickname: String,
    @SerializedName("phoneNumber") val phoneNumber: String,
    @SerializedName("profileImageId") val profileImageId: Long,
    @SerializedName("area") val area: String
)

enum class AccountType {
    local, kakao, google
}