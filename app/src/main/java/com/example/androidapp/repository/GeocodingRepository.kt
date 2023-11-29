package com.example.androidapp.repository

import android.provider.Settings.Global.getString
import android.util.Log
import com.example.androidapp.BuildConfig
import com.example.androidapp.R
import com.example.androidapp.network.GeocodingApiService
import com.example.androidapp.network.GeocodingResponse
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Call  //I'm not sure this is correct!
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import kotlinx.serialization.json.Json
import kotlin.Result
import androidx.core.util.Preconditions



class GeocodingRepository {
    private val apiKey = BuildConfig.MAPS_API_KEY

    private val json = Json { ignoreUnknownKeys = true }
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://maps.googleapis.com/") // Base URL for the API
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType())) // Kotlin Serialization converter
        .build()

    private val geocodingApiService = retrofit.create(GeocodingApiService::class.java)


    suspend fun fetchCoordinates(address: String): Result<Pair<Double, Double>> {
        return try {
                val result = withContext(Dispatchers.IO){
                    geocodingApiService.getCoordinates(address, apiKey)
                }
                Log.d("status", "geoResult: ${result.status}")

                 if (result.status == "OK" && result.results.isNotEmpty()) {
                        val location = result.results[0].geometry.location
                     Log.d("geo", "$location")
                       Result.success(Pair(location.lat, location.lng))
                    } else {
                        Result.failure(Exception("no results"))

                    }
            } catch (e: Exception) {
                Result.failure(e)

            }
        }

    }


