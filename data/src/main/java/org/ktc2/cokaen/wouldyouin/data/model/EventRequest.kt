package org.ktc2.cokaen.wouldyouin.data.model

import java.time.LocalDateTime

data class EventRequest(
    val startTime: List<String>,
    val endTime: List<String>,
    val location: Location,
    val title: String,
    val content: String,
    val category: String,
    val price: Number,
    val eventImages: List<String>,
    val totalSeats: Number
)
