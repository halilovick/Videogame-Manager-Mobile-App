package ba.etf.rma23.projekat

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import java.lang.reflect.Type
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class GameDeserializer : JsonDeserializer<Game> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Game {
        val jsonObject = json?.asJsonObject ?: JsonObject()

        val id = jsonObject.get("id")?.asInt ?: 0

        val gameName = jsonObject.get("name")?.asString ?: "Information unavailable"
        val genreArray = jsonObject.getAsJsonArray("genres")
        val genre = if (genreArray != null && genreArray.size() > 0) {
            val genreObject = genreArray[0].asJsonObject
            genreObject.get("name")?.asString ?: "Information unavailable"
        } else {
            "Information unavailable"
        }

        val platformArray = jsonObject.getAsJsonArray("platforms")
        val platform = if (platformArray != null && platformArray.size() > 0) {
            val platformObject = platformArray[0].asJsonObject
            platformObject.get("name")?.asString ?: "Information unavailable"
        } else {
            "Information unavailable"
        }

        val epochTimeSeconds = jsonObject.getAsJsonPrimitive("first_release_date")?.asLong ?: 0
        val releaseDate = Date(epochTimeSeconds * 1000)
        val ld = LocalDate.ofEpochDay(epochTimeSeconds / 86400)
        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        val formattedDate = ld.format(formatter)

        val ratingUnrounded = jsonObject.get("rating")?.asFloat ?: -1f
        val rating =
            BigDecimal(ratingUnrounded.toString()).setScale(2, RoundingMode.HALF_EVEN).toDouble()
        val cover = jsonObject.getAsJsonObject("cover")?.get("url")?.asString ?: "/eagle-sensors.com/wp-content/uploads/unavailable-image.jpg"
        val summary = jsonObject.get("summary")?.asString ?: "Information unavailable"

        val companyArray = jsonObject.getAsJsonArray("involved_companies")
        var developer = "Information unavailable"
        var publisher = "Information unavailable"
        if (companyArray != null && companyArray.size() > 0) {
            for (i in 0 until companyArray.size()) {
                val companyObject = companyArray[i].asJsonObject
                if (companyObject.get("developer").asBoolean == true) {
                    developer =
                        companyObject.getAsJsonObject("company")?.get("name")?.asString ?: "Information unavailable"
                }
                if (companyObject.get("publisher")?.asBoolean == true) {
                    publisher =
                        companyObject.getAsJsonObject("company")?.get("name")?.asString ?: "Information unavailable"
                }
            }
        }

        val ageRatingArray = jsonObject.getAsJsonArray("age_ratings")
        val ageRatingCategory = if (ageRatingArray != null && ageRatingArray.size() > 0) {
            val ageRatingObject = ageRatingArray[0].asJsonObject
            ageRatingObject.get("category")?.asInt ?: 0
        } else {
            0
        }

        var esrbRatings = mutableListOf("RP", "EC", "E", "E10", "T", "M", "AO")
        var pegiRatings = mutableListOf("Three", "Seven", "Twelve", "Sixteen", "Eighteen")
        var esrbRatingEnum = 0
        var esrbRating = "Information unavailable"

        if (ageRatingCategory == 1) {
            esrbRatingEnum = if (ageRatingArray.size() > 0) {
                val ageRatingObject = ageRatingArray[0].asJsonObject
                ageRatingObject.get("rating")?.asInt ?: 0
            } else {
                0
            }
            if (esrbRatingEnum >= 6 && esrbRatingEnum <= 12) {
                esrbRatingEnum -= 6
            }
            esrbRating = esrbRatings[esrbRatingEnum]
        } else if (ageRatingCategory == 2) {
            esrbRatingEnum = if (ageRatingArray.size() > 0) {
                val ageRatingObject = ageRatingArray[0].asJsonObject
                ageRatingObject.get("rating")?.asInt ?: 0
            } else {
                0
            }
            if (esrbRatingEnum >= 1 && esrbRatingEnum <= 5) {
                esrbRatingEnum -= 1
            }
            esrbRating = pegiRatings[esrbRatingEnum]
        }

        return Game(
            id,
            gameName,
            platform,
            formattedDate,
            rating,
            cover,
            esrbRating,
            developer,
            publisher,
            genre,
            summary,
            mutableListOf()
        )
    }
}
