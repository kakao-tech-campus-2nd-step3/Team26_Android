package org.ktc2.cokaen.wouldyouin.data.model

data class SocialAdditionalInfoRequest(
    val identifier: MemberIdentifier,
    val request: MemberAdditionalInfoRequest
)
