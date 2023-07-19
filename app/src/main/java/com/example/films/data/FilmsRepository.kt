package com.example.films.data

import androidx.paging.PagingData
import com.example.films.data.entities.Film
import com.example.films.data.entities.FilmDetails
import kotlinx.coroutines.flow.Flow

interface FilmsRepository {

    suspend fun loadFilms()

    fun getFilms(filterBy: Filter, query: String? = null): Flow<PagingData<Film>>

    fun getFavoriteFilms(filterBy: Filter, query: String? = null): Flow<PagingData<Film>>

    suspend fun addToFavorites(film: Film)

    suspend fun removeFromFavorites(film: Film)

    suspend fun getFilmDetails(filmId: Long): FilmDetails

}