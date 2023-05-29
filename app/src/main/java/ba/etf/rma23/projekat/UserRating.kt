package com.example.videogameview

data class UserRating(
    override val username: String,
    override val timestamp: Long,
    val rating: Double
) : UserImpression()
