package ba.etf.rma23.projekat.data.repositories

import com.google.gson.annotations.SerializedName

data class GameBodyResponse (
    @SerializedName("game") val game : AccountGameResponse
)