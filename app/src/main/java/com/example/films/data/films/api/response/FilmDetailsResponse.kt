package com.example.films.data.films.api.response

import com.example.films.data.films.entities.Actor
import com.example.films.data.films.entities.Film
import com.example.films.data.films.entities.FilmDetails

data class FilmDetailsResponse(
    val id: Long,
    val name: String,
    val poster: Poster,
    val rating: Rating,
    val year: Int,
    val genres: List<Genre>,
    val countries: List<Country>,
    val movieLength: Int,
    val ageRating: Int,
    val description: String,
    val persons: List<Person>,
    val votes: Votes,
    val premiere: Premiere,
    val videos: Videos,
) {
    fun toFilmDetails(isFavorite: Boolean) = FilmDetails(
        film = Film(
            isFavorite = isFavorite,
            id = id,
            name = name,
            posterUrl = poster.url,
            rating = rating.kp.toString(),
            year
            ),
        genres = genres.map { it.name },
        countries = countries.map { it.name },
        length = movieLength,
        ageRating = ageRating,
        description = description,
        actors = getActors(),
        votes = votes.kp,
        releaseDate = premiere.world,
        trailerUrl = videos.trailers?.firstOrNull { it.type == "TRAILER" }?.url
    )

    private fun getActors(): List<Actor> = persons.filter {
        it.enProfession == "actor"
    }.map {
        Actor(
            realName = it.name,
            imageUrl = it.photo,
            characterName = it.description
        )
    }
}