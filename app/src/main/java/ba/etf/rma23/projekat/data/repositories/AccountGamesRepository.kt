package ba.etf.rma23.projekat.data.repositories

import ba.etf.rma23.projekat.Game
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object AccountGamesRepository {
    private var userHash: String? = "f84654a6-73a4-45ac-ab4f-84fa58850896"
    val savedGames: MutableList<Game> = emptyList<Game>().toMutableList()
    var userAge: Int? = null

    fun setHash(acHash: String): Boolean {
        userHash = acHash
        return true
    }

    fun getHash(): String? {
        return userHash
    }

    suspend fun getSavedGames(): List<Game> {
        return withContext(Dispatchers.IO) {
            var response = AccountApiConfig.retrofit.getSavedGames(userHash!!)
            val lista: MutableList<Game> = mutableListOf()
            for (i in 0 until response.size) {
                lista.add(GamesRepository.getGamesByName(response[i].name)[0])
            }
            return@withContext lista
        }
    }

    suspend fun saveGame(game: Game): AccountGameResponse? {
        return withContext(Dispatchers.IO) {
            var response = AccountApiConfig.retrofit.addGameToAccount(
                userHash!!,
                GameBodyResponse(AccountGameResponse(game.id, game.title))
            )
            savedGames.add(game)
            val responseBody = response.body()
            return@withContext responseBody
        }
    }

    suspend fun removeGame(game: Game): Boolean {
        return withContext(Dispatchers.IO) {
            var response = AccountApiConfig.retrofit.removeGame(userHash!!, game.id.toString())
            var responseBody = response.body()
            savedGames.remove(game)
            if (responseBody?.response == "Games deleted") return@withContext true
            return@withContext false
        }
    }

    fun removeNonSafe(): Boolean {
        val safeGames =
            savedGames.filter { GamesRepository.esrbToNumber(it.esrbRating) >= (userAge ?: 3) }
        val removedCount = savedGames.size - safeGames.size
        savedGames.clear()
        savedGames.addAll(safeGames)
        return removedCount > 0
    }

    fun getGamesContainingString(query: String): List<Game> {
        return savedGames.filter { it.title.contains(query, ignoreCase = true) }
    }

    fun setAge(age: Int): Boolean {
        if (age in 3..100) {
            userAge = age
            return true
        }
        return false
    }
}
