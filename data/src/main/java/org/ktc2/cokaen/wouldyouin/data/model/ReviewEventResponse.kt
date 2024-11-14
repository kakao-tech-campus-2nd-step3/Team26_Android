package org.ktc2.cokaen.wouldyouin.data.model

data class ApiResponseBodyReviewEventSliceResponse(
    val success: Boolean,
    val data: ReviewEventSliceResponse?,
    val code: String,
    val message: String
)

data class ReviewEventSliceResponse(
    val reviewEvents: List<ReviewEventResponse>,
    val sliceInfo: SliceInfo
)

