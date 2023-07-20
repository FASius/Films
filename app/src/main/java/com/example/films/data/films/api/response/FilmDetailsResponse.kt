package com.example.films.data.films.api.response

import com.example.films.data.films.entities.Actor
import com.example.films.data.films.entities.Film
import com.example.films.data.films.entities.FilmDetails

data class FilmDetailsResponse(
    val id: Long,
    val name: String?,
    val poster: Poster?,
    val rating: Rating?,
    val year: Int?,
    val genres: List<Genre>?,
    val countries: List<Country>?,
    val movieLength: Int?,
    val ageRating: Int?,
    val description: String?,
    val persons: List<Person>?,
    val votes: Votes?,
    val premiere: Premiere?,
    val videos: Videos?,
) {
    fun toFilmDetails() = FilmDetails(
        film = Film(
            id = id,
            name = name,
            posterUrl = poster?.url,
            rating = rating?.kp,
            year
        ),
        genres = genres?.map { it.name } ?: emptyList(),
        countries = countries?.map { it.name } ?: emptyList(),
        length = movieLength,
        ageRating = ageRating,
        description = description,
        actors = getActors() ?: emptyList(),
        votes = votes?.kp,
        releaseDate = premiere?.world,
        trailerUrl = videos?.trailers?.firstOrNull { it.type == "TRAILER" }?.url
    )

    private fun getActors(): List<Actor>? = persons?.filter {
        it.enProfession == "actor"
    }?.map {
        Actor(
            realName = it.name,
            imageUrl = it.photo,
            characterName = it.description
        )
    }
}