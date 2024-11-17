package org.ktc2.cokaen.wouldyouin.data.model

import com.google.gson.annotations.SerializedName

data class ApiResponseBodyMemberResponse(
    val success: Boolean,
    val data: MemberResponse?,
    val code: String,
    val message: String
)

data class MemberResponse(
    val memberId: Long,
    val nickname: String,
    val phoneNumber: String,
    val profileUrl: String,
    val memberType: String,
    val email: String,
    val area: String,
    val gender: String,
    val intro: String,
    val likes: Int,
    val hashtag: List<String>
)

enum class MemberType {
    welcome, normal, curator, host, admin
}

enum class Area {
    전체, 서울, 인천, 경기도, 강원도, 충청남도, 충청북도, 경상북도, 대전, 대구, 울산, 전라북도, 경상남도, 부산, 광주, 전라남도, 제주도
}


// 일반 유저
//data class NormalMemberResponse(
//    @SerializedName("member_id")
//    override val memberId: String,
//    override val nickname: String,
//    @SerializedName("phone_number")
//    override val phoneNumber: String,
//    @SerializedName("profile_url")
//    override val profileUrl: String,
//    @SerializedName("member_type")
//    override val memberType: MemberTypeResponse = MemberTypeResponse.NORMAL,
//    val area: String,
//    val gender: GenderResponse
//) : MemberResponse()
//
//// 주최자
//data class HostMemberResponse(
//    @SerializedName("member_id")
//    override val memberId: String,
//    override val nickname: String,
//    @SerializedName("phone_number")
//    override val phoneNumber: String,
//    @SerializedName("profile_url")
//    override val profileUrl: String,
//    @SerializedName("member_type")
//    override val memberType: MemberTypeResponse = MemberTypeResponse.HOST,
//    val intro: String,
//    val followers: Int,
//    val hashtag: List<String>
//) : MemberResponse()
//
//// 큐레이터
//data class CuratorMemberResponse(
//    @SerializedName("member_id")
//    override val memberId: String,
//    override val nickname: String,
//    @SerializedName("phone_number")
//    override val phoneNumber: String,
//    @SerializedName("profile_url")
//    override val profileUrl: String,
//    @SerializedName("member_type")
//    override val memberType: MemberTypeResponse = MemberTypeResponse.CURATOR,
//    val area: String,
//    val gender: GenderResponse,
//    val intro: String,
//    val followers: Int
//) : MemberResponse()