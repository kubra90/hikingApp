package com.example.androidapp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.androidapp.model.Place
import com.example.androidapp.repository.PlaceRepository

class MyViewModel(private val placeRepository: PlaceRepository) : ViewModel() {

    var places by mutableStateOf<List<Place>>(emptyList())
    var dataLoaded by mutableStateOf(false)

    fun fetchPlaces(
        address: String,
        radius: Int?,
        onSuccess: (List<Place>) -> Unit,
    onError: (String) -> Unit
    ) {
        placeRepository.fetchPlaces(
            address,
            radius,
            onSuccess = { placesList ->
                places = placesList
                dataLoaded = true
                onSuccess(placesList)
            },
            onError = { error ->
                //handle error
                dataLoaded = true
                onError(error)
            }

        )

    }


    //onDataLoaded
    fun onDataLoaded(newPlaces: List<Place>, newDataLoaded: Boolean){
        places = newPlaces
        dataLoaded = newDataLoaded
    }
}

