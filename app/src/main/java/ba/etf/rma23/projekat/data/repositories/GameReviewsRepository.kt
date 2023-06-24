package ba.etf.rma23.projekat.data.repositories

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object GameReviewsRepository {
    suspend fun getOfflineReviews(context: Context): List<GameReview> {
        return withContext(Dispatchers.IO) {
            var db = AppDatabase.getInstance(context)
            var reviews = db!!.gameDao().getAll()
            reviews.filter { !it.online }
            return@withContext reviews
        }
    }

    suspend fun sendReview(context: Context, review: GameReview): Boolean {
        return withContext(Dispatchers.IO) {
            var reviews = AppDatabase.getInstance(context).gameDao().getAll()
            try {
                println(review.review)
                var savedGames = AccountGamesRepository.getSavedGames()
                for (game in savedGames) {
                    if (game.id == review.igdb_id) {
                        var response =
                            AccountApiConfig.retrofit.sendReview(
                                AccountGamesRepository.getHash()!!,
                                review.igdb_id.toString(),
                                ReviewBody(review.review, review.rating)
                            )
                        review.online = true
                        if (review in reviews == false) {
                            AppDatabase.getInstance(context).gameDao().insert(review)
                        }
                        return@withContext true
                    }
                }
                AccountGamesRepository.saveGame(GamesRepository.getGameById(review.igdb_id).get(0))
                AccountApiConfig.retrofit.sendReview(
                    AccountGamesRepository.getHash()!!,
                    review.igdb_id.toString(),
                    ReviewBody(review.review, review.rating)
                )
                review.online = true
                if (review in reviews == false) {
                    AppDatabase.getInstance(context).gameDao().insert(review)
                }
                return@withContext true
            } catch (exception: Exception) {
                //review.online = false
                if (review in reviews == false) {
                    AppDatabase.getInstance(context).gameDao().insert(review)
                }
                return@withContext false
            }
        }
    }

    suspend fun sendReviewWithoutAddingToDB(context: Context, review: GameReview): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                var savedGames = AccountGamesRepository.getSavedGames()
                for (game in savedGames) {
                    if (game.id == review.igdb_id) {
                        var response =
                            AccountApiConfig.retrofit.sendReview(
                                AccountGamesRepository.getHash()!!,
                                review.igdb_id.toString(),
                                ReviewBody(review.review, review.rating)
                            )
                        review.online = true
                        return@withContext true
                    }
                }
                AccountGamesRepository.saveGame(GamesRepository.getGameById(review.igdb_id).get(0))
                AccountApiConfig.retrofit.sendReview(
                    AccountGamesRepository.getHash()!!,
                    review.igdb_id.toString(),
                    ReviewBody(review.review, review.rating)
                )
                review.online = true
                return@withContext true
            } catch (exception: Exception) {
                review.online = false
                return@withContext false
            }
        }
    }

    suspend fun sendOfflineReviews(context: Context): Int {
        var lista = getOfflineReviews(context)
        var brojac = 0
        for (review in lista) {
            if (!review.online) {
                if (sendReviewWithoutAddingToDB(context, review)) {
                    AppDatabase.getInstance(context).gameDao().updateOnlineStatus(review.id!!)
                    brojac += 1
                }
            }
        }
        return brojac
    }

    suspend fun getReviewsForGame(igdb_id: Int): List<GameReview> {
        return withContext(Dispatchers.IO) {
            var response =
                AccountApiConfig.retrofit.getReviewsForGame(igdb_id.toString())
            return@withContext response.body()!!
        }
    }
}