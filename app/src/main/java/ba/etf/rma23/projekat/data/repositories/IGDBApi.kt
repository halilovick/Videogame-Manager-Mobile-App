package ba.etf.rma23.projekat.data.repositories

import ba.etf.rma23.projekat.Game
import retrofit2.Response
import retrofit2.http.*

interface IGDBApi {
    @Headers(
        "Client-ID: p8l0l85wtm6jp46lm8gaq4gnp88uoq",
        "Authorization: Bearer 6j8qzymgj8ftlxie0j8i6ki3ruwqc8",
        "Content-Type: application/json"
    )
    @GET("games")
    suspend fun getGamesByName(
        @Query("search") name: String,
        @Query("fields") fields: String = "id, name, genres.name, platforms.name, first_release_date, rating, cover.url, summary, involved_companies.developer, involved_companies.publisher, involved_companies.company.name, age_ratings.category, age_ratings.rating"
    ): Response<List<Game>>

    @POST("games")
    @Headers(
        "Client-ID: p8l0l85wtm6jp46lm8gaq4gnp88uoq",
        "Authorization: Bearer 6j8qzymgj8ftlxie0j8i6ki3ruwqc8",
        "Content-Type: application/json"
    )
    suspend fun getGameById(
        @Body body: okhttp3.RequestBody
    ): Response<List<Game>>
}