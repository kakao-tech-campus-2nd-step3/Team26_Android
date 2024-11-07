package org.ktc2.cokaen.wouldyouin.data.model

import com.google.gson.annotations.SerializedName

data class CuratorRequest(
    @SerializedName("identifier") val identifier: MemberIdentifier,
    @SerializedName("request") val request: CuratorEditRequest
)
