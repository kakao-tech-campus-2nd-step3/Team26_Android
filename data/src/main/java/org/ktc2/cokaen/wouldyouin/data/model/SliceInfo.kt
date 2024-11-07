package org.ktc2.cokaen.wouldyouin.data.model

import com.google.gson.annotations.SerializedName

data class SliceInfo(
    // 추후 수정 필요!!!!!!
    @SerializedName("totalCount")
    val totalCount: Int,

    @SerializedName("currentPage")
    val currentPage: Int,

    @SerializedName("pageSize")
    val pageSize: Int
)