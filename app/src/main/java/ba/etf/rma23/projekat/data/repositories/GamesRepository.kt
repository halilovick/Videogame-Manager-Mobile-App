package ba.etf.rma23.projekat.data.repositories

import ba.etf.rma23.projekat.Game
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object GamesRepository {
    var games: MutableList<Game> = mutableListOf()

    suspend fun GamesRepository() {
        getGamesByName("")
    }

    suspend fun getGamesByName(name: String): List<Game> {
        return withContext(Dispatchers.IO) {
            var response = IGDBApiConfig.retrofit.getGamesByName(name)
            var responseBody = response.body()
            games = (responseBody as MutableList<Game>?)!!
            return@withContext responseBody ?: emptyList()
        }
    }

    suspend fun getGamesSafe(name: String): List<Game> {
        return withContext(Dispatchers.IO) {
            var response = IGDBApiConfig.retrofit.getGamesByName(name)
            var responseBody = response.body()
            val filtered =
                responseBody?.filter { game -> esrbToNumber(game.esrbRating) <= AccountGamesRepository.userAge!! }
            return@withContext filtered ?: emptyList()
        }
    }

    suspend fun sortGames(): List<Game> {
        val saved = AccountGamesRepository.getSavedGames().toMutableList()
        saved.sortBy { it.title }
        games = getGamesByName("") as MutableList<Game>
        games.removeAll(saved)
        games.sortBy { it.title }
        saved.addAll(games)
        return saved
    }

    fun esrbToNumber(s: String): Int {
        if (s == "RP") return 0
        else if (s == "E10") return 10
        else if (s == "E") return 0
        else if (s == "T") return 13
        else if (s == "M") return 17
        else if (s == "AO") return 18
        else if (s == "Three") return 3
        else if (s == "Seven") return 7
        else if (s == "Twelve") return 12
        else if (s == "Sixteen") return 16
        else if (s == "Eighteen") return 18
        return 0;
    }
}