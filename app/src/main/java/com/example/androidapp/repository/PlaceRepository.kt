package com.example.androidapp.repository

import PlaceResponse
import android.util.Log
import com.example.androidapp.BuildConfig
import com.example.androidapp.model.Place
import com.example.androidapp.network.PlaceApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit




class PlaceRepository(private val geocodingRepository: GeocodingRepository) {

    private val json = Json { ignoreUnknownKeys = true }

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://maps.googleapis.com/")
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType())) // Kotlin Serialization converter
        .build()
    private val placeApiService: PlaceApiService = retrofit.create(PlaceApiService::class.java)


    suspend fun fetchPlaces(address: String, radius: Int? = null): Result<List<Place>> {
        return try {
            val coordinatesResult = geocodingRepository.fetchCoordinates(address)
            coordinatesResult.fold(
                onSuccess = { (latitude, longitude) ->
//                    val location = "${latitude},${longitude}"
                    val location = "%.7f,%7f".format(latitude, longitude)

                    Log.d("P", location)
                    val apiKey = BuildConfig.MAPS_API_KEY
                    Log.d("P", apiKey)
                    val type = "tourist_attraction" // Example type
                    val keyword = "hiking trails"   // Example keyword

                    val placeResponse = withContext(Dispatchers.IO) {
                        placeApiService.getPlaces(location, apiKey, type, keyword, radius)
                    }
                      Log.d("Placestatus", "status: ${placeResponse.status}")
                    Log.d("PlaceRepository", "API Response: $placeResponse")

                    if (placeResponse.status == "OK" && placeResponse.results.isNotEmpty()) {
                        Log.d("status", placeResponse.status)
                        val placesList = placeResponse.results.map { placeResult ->
                            // Convert PlaceResponse to Place object
                            // Example: Place(name = placeResult.name, ...)
                            Place(
                                name = placeResult.name,
                                rating = placeResult.rating,
                                userRatingsTotal = placeResult.userRatingsTotal,
//                                openingHours = placeResult.openingHours
                            )

                        }
                        Log.d("Place", "Places list: $placesList")
                        Result.success(placesList)

                    } else {
                        Result.failure(Exception("No results found"))
                    }
                },
                onFailure = { exception ->
                    Result.failure(exception)
                }
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}




