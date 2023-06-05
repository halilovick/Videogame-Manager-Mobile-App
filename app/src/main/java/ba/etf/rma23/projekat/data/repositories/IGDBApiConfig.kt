package ba.etf.rma23.projekat.data.repositories

import ba.etf.rma23.projekat.Game
import ba.etf.rma23.projekat.GameDeserializer
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object IGDBApiConfig {
    private const val BASE_URL =
        "https://api.igdb.com/v4/"

    val gson = GsonBuilder()
        .registerTypeAdapter(Game::class.java, GameDeserializer())
        .create()

    val retrofit: IGDBApi = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
        .create(IGDBApi::class.java)
}