package com.example.films.data.films.db.favirites.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.films.data.films.entities.Actor
import com.example.films.data.films.entities.Film
import com.example.films.data.films.entities.FilmDetails
import java.util.Date


@Entity(
    tableName = "favorites_films",
    indices = [
        Index("release_date"),
        Index("rating"),
        Index("votes")
    ]
)
data class FavoriteFilmsDbEntity(
    @ColumnInfo("id") @PrimaryKey
    val id: Long,
    @ColumnInfo("name")
    val name: String?,
    @ColumnInfo("poster_url")
    val posterUrl: String?,
    @ColumnInfo("rating")
    val rating: Double?,
    @ColumnInfo("year")
    val year: Int?,
    @ColumnInfo("genres")
    val genres: List<String>,
    @ColumnInfo("countries")
    val countries: List<String>,
    @ColumnInfo("length")
    val length: Int?,
    @ColumnInfo("age_rating")
    val ageRating: Int?,
    @ColumnInfo("description")
    val description: String?,
    @ColumnInfo("actors")
    val actors: List<Actor>,
    @ColumnInfo("votes")
    val votes: Int?,
    @ColumnInfo("release_date")
    val releaseDate: Date?,
    @ColumnInfo("trailer_url")
    val trailerUrl: String?
) {
    fun toFilmDetails() = FilmDetails(
        film = Film(id, name, posterUrl, rating, year),
        genres = genres,
        countries = countries,
        length = length,
        ageRating = ageRating,
        description = description,
        actors = actors,
        votes = votes,
        releaseDate = releaseDate,
        trailerUrl = trailerUrl
    )

    companion object {
        fun fromFilmDetails(details: FilmDetails) = FavoriteFilmsDbEntity(
            id = details.film.id,
            name = details.film.name,
            posterUrl = details.film.posterUrl,
            rating = details.film.rating,
            year = details.film.year,
            genres = details.genres,
            countries = details.countries,
            length = details.length,
            ageRating = details.ageRating,
            description = details.description,
            actors = details.actors,
            votes = details.votes,
            releaseDate = details.releaseDate,
            trailerUrl = details.trailerUrl
        )
    }
}