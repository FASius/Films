package com.example.films.data.films.api.response

import com.example.films.data.films.entities.Film

data class Doc(
    val id: Long,
    val name: String?,
    val poster: Poster?,
    val rating: Rating?,
    val year: Int?
) {
    fun toFilm(isFavorite: Boolean) = Film(
        isFavorite = isFavorite,
        id = id,
        name = name,
        posterUrl = poster?.previewUrl,
        rating = rating?.kp.toString(),
        year = year
    )
}