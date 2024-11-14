package org.ktc2.cokaen.wouldyouin.data.model

import com.google.gson.annotations.SerializedName

data class Location(
    val detailAddress: String,
    val latitude: Double,
    val longitude: Double
)

data class LocationFilter(
    @SerializedName("startLatitude") val startLatitude: Double,
    @SerializedName("startLongitude") val startLongitude: Double,
    @SerializedName("endLatitude") val endLatitude: Double,
    @SerializedName("endLongitude") val endLongitude: Double,
    @SerializedName("startLatitudeLessThanEndLatitude") val startLatitudeLessThanEndLatitude: Boolean,
    @SerializedName("startLongitudeLessThanEndLongitude") val startLongitudeLessThanEndLongitude: Boolean
)
