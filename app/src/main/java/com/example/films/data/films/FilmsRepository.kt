package com.example.films.data.films

import androidx.paging.PagingData
import com.example.films.data.films.entities.Film
import com.example.films.data.films.entities.FilmDetails
import kotlinx.coroutines.flow.Flow

interface FilmsRepository {

    suspend fun loadFilms()

    fun getFilms(filterBy: Filter, query: String? = null): Flow<PagingData<Film>>

    fun getFavoriteFilms(filterBy: Filter, query: String? = null): Flow<PagingData<Film>>

    suspend fun addToFavorites(film: Film)

    suspend fun removeFromFavorites(film: Film)

    suspend fun getFilmDetails(filmId: Long): FilmDetails

}