package org.ktc2.cokaen.wouldyouin.data.model

data class Event(
    val startTime: String,
    val endTime: String,
    val location: Location,
    val title: String,
    val content: String,
    val category: String,
    val price: Number,
    val eventImages: List<String>,
    val totalSeats: Number
)
