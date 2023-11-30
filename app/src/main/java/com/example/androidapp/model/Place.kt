package com.example.androidapp.model

//import OpeningHours

data class Place(
    val name: String,
    val rating: Double,
    val userRatingsTotal: Long,
//    val openingHours: OpeningHours?
    val latitude: Double,
    val longitude: Double
)