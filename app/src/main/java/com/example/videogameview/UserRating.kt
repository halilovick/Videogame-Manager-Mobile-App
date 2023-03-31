package com.example.videogameview

data class UserRating(
    override val username: String,
    override val timestamp: Long,
    override val rating: Double
) : UserImpression(username, timestamp,rating,null)
