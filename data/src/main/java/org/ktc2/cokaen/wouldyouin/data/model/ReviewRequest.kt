package org.ktc2.cokaen.wouldyouin.data.model
import com.google.gson.annotations.SerializedName

data class ReviewCreateRequest(
    @SerializedName("eventId")
    val eventId: Long,

    @SerializedName("score")
    val score: Int,

    @SerializedName("content")
    val content: String
)

data class ReviewEditRequest(
    @SerializedName("score")
    val score: Int,

    @SerializedName("content")
    val content: String
)
