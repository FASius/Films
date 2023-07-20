package com.example.films.data.films.db.favirites.entities

import androidx.room.ColumnInfo
import com.example.films.data.films.entities.Film

data class FavoriteFilmTuple(
    @ColumnInfo("id")
    val id: Long,
    @ColumnInfo("name")
    val name: String?,
    @ColumnInfo("poster_url")
    val posterUrl: String?,
    @ColumnInfo("rating")
    val rating: Double?,
    @ColumnInfo("year")
    val year: Int?
) {
    fun toFilm() = Film(
        id,
        name,
        posterUrl,
        rating,
        year
    )
}