package com.example.androidapp.network

import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.Call

interface GeocodingApiService {

    //this below code is for getting coordinates
    @GET("maps/api/geocode/json")
    fun getCoordinates(
        @Query("address") address: String,
        @Query("key") apiKey: String
    ): Call<GeocodingResponse>
}
