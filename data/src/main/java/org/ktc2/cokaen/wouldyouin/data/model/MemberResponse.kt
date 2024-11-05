package org.ktc2.cokaen.wouldyouin.data.model

import com.google.gson.annotations.SerializedName

enum class MemberTypeResponse {
    @SerializedName("normal") NORMAL,
    @SerializedName("curator") CURATOR,
    @SerializedName("host") HOST
}

enum class GenderResponse {
    @SerializedName("Man") MALE,
    @SerializedName("Woman") FEMALE
}

sealed class MemberResponse {
    abstract val memberId: String
    abstract val nickname: String
    abstract val phoneNumber: String
    abstract val profileUrl: String
    abstract val memberType: MemberTypeResponse
}

// 일반 유저
data class NormalMemberResponse(
    @SerializedName("member_id")
    override val memberId: String,
    override val nickname: String,
    @SerializedName("phone_number")
    override val phoneNumber: String,
    @SerializedName("profile_url")
    override val profileUrl: String,
    @SerializedName("member_type")
    override val memberType: MemberTypeResponse = MemberTypeResponse.NORMAL,
    val area: String,
    val gender: GenderResponse
) : MemberResponse()

// 주최자
data class HostMemberResponse(
    @SerializedName("member_id")
    override val memberId: String,
    override val nickname: String,
    @SerializedName("phone_number")
    override val phoneNumber: String,
    @SerializedName("profile_url")
    override val profileUrl: String,
    @SerializedName("member_type")
    override val memberType: MemberTypeResponse = MemberTypeResponse.HOST,
    val intro: String,
    val followers: Int,
    val hashtag: List<String>
) : MemberResponse()

// 큐레이터
data class CuratorMemberResponse(
    @SerializedName("member_id")
    override val memberId: String,
    override val nickname: String,
    @SerializedName("phone_number")
    override val phoneNumber: String,
    @SerializedName("profile_url")
    override val profileUrl: String,
    @SerializedName("member_type")
    override val memberType: MemberTypeResponse = MemberTypeResponse.CURATOR,
    val area: String,
    val gender: GenderResponse,
    val intro: String,
    val followers: Int
) : MemberResponse()