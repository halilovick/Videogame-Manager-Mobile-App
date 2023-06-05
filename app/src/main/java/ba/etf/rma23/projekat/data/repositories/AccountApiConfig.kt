package ba.etf.rma23.projekat.data.repositories

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object AccountApiConfig {
    private const val BASE_URL =
        "https://rma23ws.onrender.com"

    /*val gson = GsonBuilder()
        .registerTypeAdapter(Game::class.java, GameDeserializer())
        .create()

     */
    val retrofit: AccountApi = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(AccountApi::class.java)
}