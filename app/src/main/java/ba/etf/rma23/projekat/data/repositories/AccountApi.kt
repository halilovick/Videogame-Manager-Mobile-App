package ba.etf.rma23.projekat.data.repositories

import retrofit2.Response
import retrofit2.http.*

interface AccountApi {
    @GET("/account/{aid}/games")
    suspend fun getSavedGames(@Path("aid") aid: String): List<AccountGameResponse>

    @POST("/account/{aid}/game")
    suspend fun addGameToAccount(@Path("aid") aid: String, @Body game: GameBodyResponse): Response<AccountGameResponse>

    @DELETE("/account/{aid}/game/{gid}")
    suspend fun removeGame(
        @Path("aid") aid: String,
        @Path("gid") gameId: String
    ): Response<DeleteGameResponse>

    @DELETE("/account/{aid}/game")
    suspend fun deleteAllGamesFromAccount(@Path("aid") aid: String): Boolean

    //@POST("/login")
    //suspend fun login(@Path("aid") aid: String):
}