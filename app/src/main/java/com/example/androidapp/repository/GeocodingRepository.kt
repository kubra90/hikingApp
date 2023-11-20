package com.example.androidapp.repository

import android.provider.Settings.Global.getString
import com.example.androidapp.BuildConfig
import com.example.androidapp.R
import com.example.androidapp.network.GeocodingApiService
import com.example.androidapp.network.GeocodingResponse
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Call  //I'm not sure this is correct!
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import kotlinx.serialization.json.Json



class GeocodingRepository {
    private val apiKey = BuildConfig.MAPS_API_KEY

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://maps.googleapis.com/") // Base URL for the API
        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType())) // Kotlin Serialization converter
        .build()



    fun fetchCoordinates(address: String,
                        onSuccess: (Double, Double) -> Unit,
                        onError: (String) -> Unit){
        val geocodingApiService = retrofit.create(GeocodingApiService::class.java)
        val call = geocodingApiService.getCoordinates(address, apiKey)

        call.enqueue(object : Callback<GeocodingResponse> {
            override fun onResponse(
                call: Call<GeocodingResponse>,
                response: Response<GeocodingResponse>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    val responseBody = response.body()!!
                    if (responseBody.status == "OK" && responseBody.results.isNotEmpty()) {
                        val location = responseBody.results[0].geometry.location
                        onSuccess(location.lat, location.lng)
                    } else {
                        // Handle the case where no results were found
                        onError("No results found for the given address.")
                    }
                } else {
                    // Handle the case where the response is not successful
                    onError("Failed to fetch coordinates: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<GeocodingResponse>, t: Throwable) {
                // Handle API call failure, such as network errors
                onError("API call failed: ${t.message}")
            }
        })
    }
}


