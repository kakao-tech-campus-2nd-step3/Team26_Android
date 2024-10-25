package org.ktc2.cokaen.wouldyouin.data.model
enum class MemberType {
    NORMAL, CURATOR, HOST
}

enum class Gender {
    MALE, FEMALE, UNKNOWN
}

sealed class Member {
    abstract val memberId: String
    abstract val nickname: String
    abstract val phoneNumber: String
    abstract val profileUrl: String
    abstract val memberType: MemberType
}

// 일반 유저
data class NormalMember(
    override val memberId: String,
    override val nickname: String,
    override val phoneNumber: String,
    override val profileUrl: String,
    override val memberType: MemberType = MemberType.NORMAL,
    val area: String,
    val gender: Gender
) : Member()

// 주최자
data class HostMember(
    override val memberId: String,
    override val nickname: String,
    override val phoneNumber: String,
    override val profileUrl: String,
    override val memberType: MemberType = MemberType.HOST,
    val intro: String,
    val followers: Int,
    val hashtags: List<String>
) : Member()

// 큐레이터
data class CuratorMember(
    override val memberId: String,
    override val nickname: String,
    override val phoneNumber: String,
    override val profileUrl: String,
    override val memberType: MemberType = MemberType.CURATOR,
    val area: String,
    val gender: Gender,
    val intro: String,
    val followers: Int
) : Member()