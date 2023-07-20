package com.example.films.data.films.api

import com.example.films.data.films.Filter
import com.example.films.data.films.api.response.FilmDetailsResponse
import com.example.films.data.films.api.response.FilmsListResponse
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FilmsDataSource @Inject constructor(
    retrofit: Retrofit
) {
    private val filmsApi = retrofit.create(FilmsApi::class.java)

    suspend fun getFilms(page: Int, limit: Int, filterBy: Filter, query: String?): FilmsListResponse {
        val filter = filterToString(filterBy)
        val filterType = if (filterBy == Filter.ALPHABET) "1" else "-1"
        return filmsApi.getFilms(query, filter, filterType, page + 1, limit)
    }

    suspend fun getFilmDetails(filmId: Long): FilmDetailsResponse {
        return filmsApi.getFilmDetails(filmId)
    }

    private fun filterToString(filterBy: Filter) = when (filterBy) {
        Filter.RATING -> "rating.kp"
        Filter.RELEASE_DATE -> "premiere.world"
        Filter.VOTES -> "votes.kp"
        Filter.ALPHABET -> "name"
    }
}