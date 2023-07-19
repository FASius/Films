package com.example.films.data.films

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.films.data.films.api.FilmsDataSource
import com.example.films.data.films.entities.Film
import com.example.films.data.films.entities.FilmDetails
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FilmsRepositoryImpl @Inject constructor(
    private val dataSource: FilmsDataSource
) : FilmsRepository {

    override suspend fun loadFilms() {
        delay(1000)
        // TODO: implement
    }

    private suspend fun loadPage(pageIndex: Int, pageSize: Int, filterBy: Filter, query: String?): FilmsPage {
        // TODO: load from db if exists
        val filmsResponse = dataSource.getFilms(pageIndex, pageSize, filterBy, query)
        return FilmsPage(
            filmsResponse.docs.map { it.toFilm(filmInFavorites(it.id)) },
            filmsResponse.page,
            filmsResponse.pages
        )
    }

    private fun filmInFavorites(filmId: Long): Boolean {
        // TODO: check isFavorite in db
        return false
    }

    override fun getFilms(filterBy: Filter, query: String?): Flow<PagingData<Film>> {
        val loader: FilmsLoader = { pageIndex, pageSize ->
            loadPage(pageIndex, pageSize, filterBy, query)
        }
        return Pager(
            config = PagingConfig(pageSize = PAGE_SIZE, initialLoadSize = INITIAL_PAGE_SIZE),
            pagingSourceFactory = {
                FilmsPagingSource(loader)
            }
        ).flow
    }

    override fun getFavoriteFilms(filterBy: Filter, query: String?): Flow<PagingData<Film>> {
        // TODO: implement
        return getFilms(filterBy, query)
    }

    override suspend fun addToFavorites(film: Film) {
        // TODO: implement
    }

    override suspend fun removeFromFavorites(film: Film) {
        // TODO: implement
    }

    override suspend fun getFilmDetails(filmId: Long): FilmDetails {
        val detailsResponse = dataSource.getFilmDetails(filmId)
        return detailsResponse.toFilmDetails(filmInFavorites(filmId))
    }

    companion object {
        private const val PAGE_SIZE = 20
        private const val INITIAL_PAGE_SIZE = PAGE_SIZE * 2
    }
}