package org.ktc2.cokaen.wouldyouin.data.model

import java.time.LocalDateTime

data class AdvertisementResponse(
    val id: Long,
    val title: String,
    val imageUrl: String,
    val startTime: String,
    val endTime: String
)

data class ApiResponseBodyListAdvertisementResponse(
    val success: Boolean,
    val data: List<AdvertisementResponse>,
    val code: String,
    val message: String
)

data class ApiResponseBodyAdvertisementResponse(
    val success: Boolean,
    val data: AdvertisementResponse,
    val code: String,
    val message: String
)
