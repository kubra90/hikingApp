package com.example.androidapp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidapp.model.Place
import com.example.androidapp.repository.PlaceRepository
import kotlinx.coroutines.launch
import android.util.Log

class MyViewModel(private val placeRepository: PlaceRepository) : ViewModel() {

    var places by mutableStateOf<List<Place>>(emptyList())
    var dataLoaded by mutableStateOf(false)
    var errorMessage by mutableStateOf("")

    fun fetchPlaces(address: String, radius: Int?) {
        dataLoaded = false
        viewModelScope.launch {
            try {
                val result = placeRepository.fetchPlaces(address, radius)
                result.onSuccess { placesList ->
                    places = placesList
                    dataLoaded = true
                    Log.d("fetch", "$places")
                }.onFailure { error ->
                    errorMessage = error.message ?: "Unknown error"
                    dataLoaded = true
                }
            } catch (e: Exception) {
                errorMessage = e.message ?: "Unknown error"
                dataLoaded = true
            }
        }
    }
}
