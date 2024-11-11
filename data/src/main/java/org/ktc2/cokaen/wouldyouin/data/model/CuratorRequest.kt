package org.ktc2.cokaen.wouldyouin.data.model

import com.google.gson.annotations.SerializedName

data class CuratorRequest(
    val id: Long,
    val type: String = "welcome"
)
