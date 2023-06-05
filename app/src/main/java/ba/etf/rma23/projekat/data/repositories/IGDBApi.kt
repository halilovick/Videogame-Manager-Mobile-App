package ba.etf.rma23.projekat.data.repositories

import ba.etf.rma23.projekat.BuildConfig
import ba.etf.rma23.projekat.Game
import retrofit2.Response
import retrofit2.http.*

interface IGDBApi {
    @Headers(
        "Client-ID: ${BuildConfig.ClientID}",
        "Authorization: ${BuildConfig.Auth}",
        "Content-Type: application/json"
    )
    @GET("games")
    suspend fun getGamesByName(
        @Query("search") name: String,
        @Query("fields") fields: String = "id, name, genres.name, platforms.name, first_release_date, rating, cover.url, summary, involved_companies.developer, involved_companies.publisher, involved_companies.company.name, age_ratings.category, age_ratings.rating"
    ): Response<List<Game>>
}