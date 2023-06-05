package ba.etf.rma23.projekat.data.repositories

import com.google.gson.annotations.SerializedName

data class AccountGameResponse(
    @SerializedName("igdb_id") val id : Int,
    @SerializedName("name") val name : String
)