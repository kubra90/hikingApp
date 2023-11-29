
package com.example.androidapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import android.util.Log

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.androidapp.repository.GeocodingRepository
import com.example.androidapp.repository.PlaceRepository
//import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.androidapp.viewmodel.MyViewModel
import com.example.androidapp.ui.theme.AndroidAppTheme
import androidx.lifecycle.viewmodel.compose.viewModel


// ...imports and class declaration



class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val factory = MyViewModelFactory(PlaceRepository(GeocodingRepository()))
            val viewModel: MyViewModel by viewModels { factory }
            AndroidAppTheme {

                MyComposeContent(viewModel = viewModel)
            }
        }
        }

    class MyViewModelFactory(private val repository: PlaceRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MyViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MyViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
    }


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyComposeContent(viewModel: MyViewModel) {
    var location by remember { mutableStateOf("") }
    var radius by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    val places = viewModel.places
    val dataLoaded = viewModel.dataLoaded
    val errorMessage = viewModel.errorMessage

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
                keyboardType = KeyboardType.Number
            )
        )

        Button(
            onClick = {
                val address = location
                val distance = radius.toIntOrNull()
                if (address.isNotEmpty() && distance != null) {
                    isLoading = true
                    viewModel.fetchPlaces(address, distance)
                }
            },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("Search")
        }

        if (!isLoading) {
            CircularProgressIndicator(modifier = Modifier.padding(16.dp))
        }

        // Display the list of places when data is loaded
        if (dataLoaded && isLoading) {
            Log.d("loading", "loading status is${!isLoading}")
         if (places.isNotEmpty()) {
             Log.d("view", "$places")
                Column {
                    Text("Places found:")
                    places.forEach { place ->
                        Text("Name: ${place.name}, Rating: ${place.rating}")
                        Log.d("TAG","Name: ${place.name}")
                        println("Name: ${place.name}")
                    }
              }
            }
            else {
                Text("No places found!")
            }
        }

        if (errorMessage.isNotEmpty()) {
            Text("Err: $errorMessage")
        }
    }
}
// ... rest of the code

@Preview(showBackground = true)
@Composable
fun MyPreview() {
    val viewModel = MyViewModel(PlaceRepository(GeocodingRepository()))

    MyComposeContent(viewModel = viewModel)
}
