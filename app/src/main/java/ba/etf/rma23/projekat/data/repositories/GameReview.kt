package ba.etf.rma23.projekat.data.repositories

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class GameReview(
    @ColumnInfo(name = "rating") @SerializedName("rating") var rating: Int?,
    @ColumnInfo(name = "review") @SerializedName("review") var review: String?,
    @ColumnInfo(name = "igdb_id") @SerializedName("GameId") var igdb_id: Int,
    @ColumnInfo(name = "online") var online: Boolean = false,
    @SerializedName("student") var student: String? = "",
    @SerializedName("timestamp") var timestamp: String? = "",
    @PrimaryKey(true) @ColumnInfo(name = "id") @SerializedName("id") var id: Int? = null
)
