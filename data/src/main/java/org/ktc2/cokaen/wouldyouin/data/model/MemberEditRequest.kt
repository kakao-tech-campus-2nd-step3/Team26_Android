package org.ktc2.cokaen.wouldyouin.data.model
import com.google.gson.annotations.SerializedName

data class MemberRequest(
    @SerializedName("identifier") val identifier: MemberIdentifier,
    @SerializedName("editRequest") val editRequest: MemberEditRequest
)

data class MemberIdentifier(
    @SerializedName("id") val id: Long,
    @SerializedName("type") val type: String
)

data class MemberEditRequest(
    @SerializedName("nickname") val nickname: String,
    @SerializedName("phoneNumber") val phoneNumber: String,
    @SerializedName("profileImageId") val profileImageId: Long,
    @SerializedName("area") val area: String
)

enum class AccountType {
    WELCOME, NORMAL, CURATOR, HOST, ADMIN
}