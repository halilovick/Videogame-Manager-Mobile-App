package com.example.videogameview

abstract class UserImpression(
    open val username: String,
    open val timestamp: Long,
    open val rating: Double?,
    open val review: String?
)



