package org.ktc2.cokaen.wouldyouin.data.model

import java.time.LocalDateTime

data class AdvertisementRequest(
    val title: String,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime,
    val endTimeAfterStartTime: Boolean
)
