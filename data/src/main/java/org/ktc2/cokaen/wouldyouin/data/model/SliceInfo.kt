package org.ktc2.cokaen.wouldyouin.data.model

import com.google.gson.annotations.SerializedName

data class SliceInfo(
    @SerializedName("sliceSize")
    val sliceSize: Int,

    @SerializedName("lastId")
    val lastId: Long
)