package com.example.androidapp

import android.content.ContentValues.TAG
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.androidapp.model.Place
import com.example.androidapp.repository.GeocodingRepository
import com.example.androidapp.repository.PlaceRepository
import com.example.androidapp.ui.theme.AndroidAppTheme
import com.example.androidapp.viewmodel.MyViewModel
import android.util.Log


class MainActivity : ComponentActivity() {

    private val TAG = "MyApp"


    private lateinit var geocodingRepository: GeocodingRepository
    private lateinit var placeRepository: PlaceRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        geocodingRepository = GeocodingRepository()
        placeRepository = PlaceRepository(geocodingRepository)

        setContent {
            AndroidAppTheme {
                // Use a ViewModel to manage UI state
                val viewModel = remember { MyViewModel(placeRepository) }


                MyComposeContent(
                    viewModel,
                    viewModel.places,
                    viewModel.dataLoaded,
                    viewModel::onDataLoaded
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyComposeContent(
    viewModel: MyViewModel,
    places: List<Place>,
    dataLoaded: Boolean,
    onDataLoaded: (List<Place>, Boolean) -> Unit
) {
    var location by remember { mutableStateOf("") }
    var radius by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var isError by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = location,
            onValueChange = { location = it },
            label = { Text("Enter city, park, or hiking trail!") }
        )

        TextField(
            value = radius,
            onValueChange = { radius = it },
            label = { Text("Radius!") },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number // Specify a numeric keyboard
            )
        )
        Button(
            onClick = {
                val address = location
                val distance = radius.toIntOrNull()
                // Fetch places using ViewModel
                if (address.isNotEmpty() && distance != null) {
                    isLoading = true
                    isError = false

                    viewModel.fetchPlaces(
                        address,
                        distance,
                        onSuccess = { places->
                            isLoading = false
                            onDataLoaded(places, true)

                        },
                        onError = {
                            isLoading = false
                            isError = true
                        }
                    )
                } else {
                    isError = true
                }
            },
            modifier = Modifier
                .padding(top = 16.dp)
        ) {
            Text("Search")
        }
        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.padding(16.dp))
        }

        // Display the list of places when data is loaded
        if (dataLoaded && !isLoading && !isError) {
            if (places.isNotEmpty()) {
                Column {
                    Text("Places found:")
//                    Log.d(TAG, "debug message")
                    places.forEach { place ->
                        Text("Name: ${place.name}, Rating: ${place.rating}")
                    }
                }
            } else {
                Log.d(TAG, "debug message")
                Text("No palces found!")
            }
        }
    }
}

@Preview
@Composable
fun MyPreview() {

    //check with sample data
//    val samplePlaces = listOf(
//    Place("Place 1", 4.5, 100, true),
//    Place("Place 2", 3.8, 80, true)
//    )
    MyComposeContent(
        viewModel = MyViewModel(placeRepository = PlaceRepository(geocodingRepository = GeocodingRepository())),
        places = emptyList(), // Initialize with an empty list of places
//        places =samplePlaces,
        dataLoaded = false, // Set dataLoaded to false
//        dataLoaded = true,
        onDataLoaded = { _, _ -> }
    )
}


