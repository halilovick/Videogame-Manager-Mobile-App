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

    @GET("/game/{gid}/gamereviews")
    suspend fun getReviewsForGame(
        @Path("gid") gid: String
    ): Response<List<GameReview>>

    @POST("/account/{aid}/game/{gid}/gamereview")
    suspend fun sendReview(
        @Path("aid") aid: String,
        @Path("gid") gid: String,
        @Body body: ReviewBody
    ): Response<GameReview>
}