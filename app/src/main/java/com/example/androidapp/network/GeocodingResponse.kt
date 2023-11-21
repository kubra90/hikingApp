package com.example.androidapp.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class GeocodingResponse(
    @SerialName("results")
    val results: List<GeocodingResult>,
    @SerialName("status")
    val status: String
)

@Serializable
data class GeocodingResult(
    @SerialName("geometry")
    val geometry: Geometry
)

@Serializable
data class Geometry(
    @SerialName("location")
    val location: Location
)

@Serializable
data class Location(
    @SerialName("lat")
    val lat: Double,
    @SerialName("lng")
    val lng: Double
)
