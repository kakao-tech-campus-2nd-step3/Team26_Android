package org.ktc2.cokaen.wouldyouin.data.model

data class SocialLoginRequest(
    val accountType: AccountType,
    val token: String
)