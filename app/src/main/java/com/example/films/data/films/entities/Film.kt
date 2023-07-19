package com.example.films.data.films.entities

data class Film(
    val isFavorite: Boolean,
    val id: Long,
    val name: String?,
    val posterUrl: String?,
    val rating: String?,
    val year: Int?
)