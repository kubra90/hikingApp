import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.androidapp.model.Place
import com.example.androidapp.repository.GeocodingRepository

class MainActivity : ComponentActivity() {
    private lateinit var geocodingRepository: GeocodingRepository
    private var places by mutableStateOf<List<Place>>(emptyList())
    private var dataLoaded by mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        geocodingRepository = GeocodingRepository()

        setContent {
            MyComposeContent(geocodingRepository, places, dataLoaded) { newPlaces, newDataLoaded ->
                places = newPlaces
                dataLoaded = newDataLoaded
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyComposeContent(
    geocodingRepository: GeocodingRepository,
    places: List<Place>,
    dataLoaded: Boolean,
    onDataLoaded: (List<Place>, Boolean) -> Unit
) {
    var location by remember { mutableStateOf("") }
    val context = LocalContext.current

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
        Button(
            onClick = {
                val address = location
                geocodingRepository.fetchCoordinates(
                    address,
                    onSuccess = { lat, lng ->
                        // Fetch places based on coordinates
                        val radius = 1000 // You can adjust the radius as needed
                        geocodingRepository.fetchPlaces(
                            lat,
                            lng,
                            radius,
                            onSuccess = { placesList ->
                                onDataLoaded(placesList, true)
                            },
                            onError = { error ->
                                // Handle error here
                            }
                        )
                    },
                    onError = { error ->
                        // Handle error here
                    }
                )
            },
            modifier = Modifier
                .padding(top = 16.dp)
        ) {
            Text("Search")
        }

        // Display the list of places when data is loaded
        if (dataLoaded) {
            Column {
                Text("Places found:")
                places.forEach { place ->
                    Text("Name: ${place.name}, Rating: ${place.rating}")
                }
            }
        }
    }
}