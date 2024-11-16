package org.ktc2.cokaen.wouldyouin.data.model

import java.time.LocalDateTime

data class AdvertisementRequest(
    val title: String,
    val startTime: String,
    val endTime: String,
    val endTimeAfterStartTime: Boolean
)
