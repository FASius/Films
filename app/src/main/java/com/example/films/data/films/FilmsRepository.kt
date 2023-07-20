package com.example.films.data.films

import androidx.paging.PagingData
import com.example.films.data.films.entities.Film
import com.example.films.data.films.entities.FilmDetails
import kotlinx.coroutines.flow.Flow

interface FilmsRepository {

    suspend fun loadFilms(filterBy: Filter, query: String? = null, pageIndex: Int = 0)

    fun getFilms(filterBy: Filter, query: String? = null): Flow<PagingData<Film>>

    fun getFavoriteFilms(filterBy: Filter, query: String? = null): Flow<PagingData<Film>>

    suspend fun addToFavorites(film: Film)

    suspend fun isFavorite(filmId: Long): Boolean

    suspend fun addToFavorites(filmDetails: FilmDetails)

    suspend fun addToFavorites(filmId: Long)

    suspend fun favoritesFilmsCount(): Int

    suspend fun removeFromFavorites(filmId: Long)

    suspend fun removeAllFavorites()

    suspend fun getFilmDetails(filmId: Long): FilmDetails

}