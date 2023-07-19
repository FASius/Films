package com.example.films.data.films.entities

import java.util.Date

data class FilmDetails(
    val film: Film,
    val genres: List<String>,
    val countries: List<String>,
    val length: Int?,
    val ageRating: Int?,
    val description: String?,
    val actors: List<Actor>,
    val votes: Int?,
    val releaseDate: Date?,
    val trailerUrl: String?
)