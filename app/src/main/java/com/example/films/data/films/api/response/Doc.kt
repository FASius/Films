package com.example.films.data.films.api.response

import com.example.films.data.films.entities.Film

data class Doc(
    val id: Long,
    val name: String?,
    val poster: Poster?,
    val rating: Rating?,
    val year: Int?
) {
    fun toFilm() = Film(
        id = id,
        name = name,
        posterUrl = poster?.previewUrl,
        rating = rating?.kp,
        year = year
    )
}