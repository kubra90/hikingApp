package com.example.androidapp.model

data class Place(
    val name: String,
    val rating: Double,
    val userRatingsTotal: Long,
    val openingHours: Any
)