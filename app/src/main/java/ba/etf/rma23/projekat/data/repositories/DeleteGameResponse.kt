package ba.etf.rma23.projekat.data.repositories

import com.google.gson.annotations.SerializedName

data class DeleteGameResponse(
    @SerializedName("success") val response: String,
)