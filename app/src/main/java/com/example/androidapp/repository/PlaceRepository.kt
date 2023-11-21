package com.example.androidapp.repository

import PlaceResponse
import android.util.Log
import com.example.androidapp.BuildConfig
import com.example.androidapp.model.Place
import com.example.androidapp.network.PlaceApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class PlaceRepository(private val geocodingRepository: GeocodingRepository) {

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://maps.googleapis.com/")
        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType())) // Kotlin Serialization converter
        .build()
    private val placeApiService: PlaceApiService = retrofit.create(PlaceApiService::class.java)

    fun fetchPlaces(
        address: String,
        radius: Int? = null,
        onSuccess: (List<Place>) -> Unit,
        onError: (String) -> Unit
    ) {
        geocodingRepository.fetchCoordinates(
            address,
            onSuccess = { lat, lng ->

                val location = "$lat%2C$lng"

                val apiKey = BuildConfig.MAPS_API_KEY
                val type = "tourist_attraction"
                val keyword = "hiking trails"
                val call = placeApiService.getPlaces(location, apiKey, type, keyword, radius)

                call.enqueue(object : Callback<PlaceResponse> {
                    override fun onResponse(
                        call: Call<PlaceResponse>,
                        response: Response<PlaceResponse>
                    ) {
                        if (response.isSuccessful && response.body() != null) {
                            val responseBody = response.body()!!
                            if (responseBody.status == "OK" && responseBody.results.isNotEmpty()) {
                                // Collect Place objects in a list
                                val placesList = responseBody.results.map { placeResult ->
                                    val placeName = placeResult.name
                                    val placeRating = placeResult.rating
                                    val placeRatingTotal = placeResult.userRatingsTotal
                                    val openingHours = placeResult.openingHours
                                    Place(placeName, placeRating, placeRatingTotal, openingHours)
                                }
                                onSuccess(placesList) // Return the list of Place objects
                            } else {
                                onError("No results found")
                            }
                        } else {
                            onError("Response is not successful")
                        }
                    }

                    override fun onFailure(call: Call<PlaceResponse>, t: Throwable) {
                        onError("Failed to fetch places: ${t.message}")
                    }
                })
            },

            onError = {
                onError("Error fetching coordinates: $it")
            }
        )
    }
}


