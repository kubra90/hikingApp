package com.example.androidapp.network

import PlaceResponse
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.Call

interface PlaceApiService {
//    companion object{
//        private const val keyword = "hiking trails"
//        private const val type = "tourist_attraction"
//    }

    //this below code is for getting place information
    @GET("maps/api/place/nearbysearch/json")
    fun getPlaces(
        @Query("location") address: String,
        @Query("key") apiKey: String,
        @Query("type") type: String,
        @Query("keyword") keyword: String,
        @Query("radius") radius: Int?,  //this is optional query parameter

    ): Call<PlaceResponse>
}