package com.example.videogameview

data class UserReview(
    override val username: String,
    override val timestamp: Long,
    override val review: String
):UserImpression(username, timestamp,null, review)

