package com.example.androidapp.network

import kotlinx.serialization.SerialName


data class GeocodingResponse(
    @SerialName("results")
    val results: List<GeocodingResult>,
    @SerialName("status")
    val status: String
)

data class GeocodingResult(
    @SerialName("geometry")
    val geometry: Geometry
)

data class Geometry(
    @SerialName("location")
    val location: Location
)

data class Location(
    @SerialName("lat")
    val lat: Double,
    @SerialName("lng")
    val lng: Double
)
