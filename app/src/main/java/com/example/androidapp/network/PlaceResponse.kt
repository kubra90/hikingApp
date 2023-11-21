
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


data class PlaceResponse(
    @SerialName("html_attributions")
    val htmlAttributions: List<Any?>,
    @SerialName("next_page_token")
    val nextPageToken: String,
    val results: List<PlaceResult>,
    val status: String,
)


data class PlaceResult(
    @SerialName("business_status")
    val businessStatus: String,
    val geometry: Geometry,
    val icon: String,
    @SerialName("icon_background_color")
    val iconBackgroundColor: String,
    @SerialName("icon_mask_base_uri")
    val iconMaskBaseUri: String,
    val name: String,
    @SerialName("opening_hours")
    val openingHours: Any,
    val photos: List<Photo>,
    @SerialName("place_id")
    val placeId: String,
    @SerialName("plus_code")
    val plusCode: PlusCode?,
    val rating: Double,
    val reference: String,
    val scope: String,
    val types: List<String>,
    @SerialName("user_ratings_total")
    val userRatingsTotal: Long,
    val vicinity: String,
)


data class Geometry(
    val location: Location,
    val viewport: Viewport,
)


data class Location(
    val lat: Double,
    val lng: Double,
)


data class Viewport(
    val northeast: Northeast,
    val southwest: Southwest,
)

data class Northeast(
    val lat: Double,
    val lng: Double,
)


data class Southwest(
    val lat: Double,
    val lng: Double,
)


data class OpeningHours(
    @SerialName("open_now")
    val openNow: Boolean,
)


data class Photo(
    val height: Long,
    @SerialName("html_attributions")
    val htmlAttributions: List<String>,
    @SerialName("photo_reference")
    val photoReference: String,
    val width: Long,
)


data class PlusCode(
    @SerialName("compound_code")
    val compoundCode: String,
    @SerialName("global_code")
    val globalCode: String,
)


