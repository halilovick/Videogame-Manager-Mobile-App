package ba.etf.rma23.projekat

data class Game(
    val id: Int,
    val title: String,
    val platform: String,
    val releaseDate: String,
    val rating: Double,
    val coverImage: String,
    val esrbRating: String,
    val developer: String,
    val publisher: String,
    val genre: String,
    val description: String,
    val userImpressions: List<UserImpression>
)

