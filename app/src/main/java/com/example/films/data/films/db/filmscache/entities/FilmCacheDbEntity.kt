package com.example.films.data.films.db.filmscache.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.films.data.films.entities.Film

@Entity("films_cache_table")
data class FilmCacheDbEntity(
    @PrimaryKey
    @ColumnInfo("id") val id: Long,
    @ColumnInfo("name") val name: String?,
    @ColumnInfo("poster_url") val posterUrl: String?,
    @ColumnInfo("rating") val rating: Double?,
    @ColumnInfo("year") val year: Int?,
    @ColumnInfo("created_at") val createdAt: Long,
    @ColumnInfo("offset") val offset: Int,
    @ColumnInfo("query") val query: String?,
    @ColumnInfo("filter") val filter: String,
) {
    fun toFilm() = Film(
        id = id,
        name = name,
        posterUrl = posterUrl,
        rating = rating,
        year = year
    )

    companion object {
        fun fromFilm(film: Film, offset: Int, query: String?, filter: String) = FilmCacheDbEntity(
            id = film.id,
            name = film.name,
            posterUrl = film.posterUrl,
            rating = film.rating,
            year = film.year,
            createdAt = System.currentTimeMillis(),
            offset = offset,
            query = query,
            filter = filter
        )
    }
}