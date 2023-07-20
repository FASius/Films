package com.example.films.data.films.entities

data class Film(
    val id: Long,
    val name: String?,
    val posterUrl: String?,
    val rating: Double?,
    val year: Int?
)