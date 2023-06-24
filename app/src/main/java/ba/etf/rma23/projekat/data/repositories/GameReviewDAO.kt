package ba.etf.rma23.projekat.data.repositories

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface GameReviewDAO {
    @Query("SELECT * FROM gamereview")
    suspend fun getAll(): List<GameReview>

    @Query("UPDATE gamereview SET online = 1 WHERE id = :id")
    suspend fun updateOnlineStatus(id : Int)

    @Insert
    suspend fun insertAll(vararg review: GameReview)

    @Insert
    suspend fun insert(gameReview: GameReview)
}