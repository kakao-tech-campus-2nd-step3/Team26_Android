package org.ktc2.cokaen.wouldyouin.data.model

import java.time.LocalDateTime

data class AdvertisementRequest(
    val title: String,
    val startTime: List<String>,
    val endTime: List<String>,
    val endTimeAfterStartTime: Boolean
)
