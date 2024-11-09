package org.ktc2.cokaen.wouldyouin.data.model

import com.google.gson.annotations.SerializedName

data class Location(
    val latitude: Double,
    val longitude: Double
)

data class LocationFilter(
    @SerializedName("startLatitude") val startLatitude: Double,
    @SerializedName("startLongitude") val startLongitude: Double,
    @SerializedName("endLatitude") val endLatitude: Double,
    @SerializedName("endLongitude") val endLongitude: Double
)
