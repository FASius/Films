package com.example.films.data.films

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.films.Constants.PAGE_SIZE
import com.example.films.data.films.api.FilmsDataSource
import com.example.films.data.films.api.response.Doc
import com.example.films.data.films.db.favirites.FavoriteFilmsDataSource
import com.example.films.data.films.db.filmscache.FilmsCacheDataSource
import com.example.films.data.films.entities.Film
import com.example.films.data.films.entities.FilmDetails
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FilmsRepositoryImpl @Inject constructor(
    private val networkDataSource: FilmsDataSource,
    private val dbDataSource: FavoriteFilmsDataSource,
    private val cacheDataSource: FilmsCacheDataSource
) : FilmsRepository {

    override suspend fun loadFilms(filterBy: Filter, query: String?, pageIndex: Int) {
        loadPage(pageIndex, PAGE_SIZE, filterBy, query)
    }

    private suspend fun loadPage(
        pageIndex: Int,
        pageSize: Int,
        filterBy: Filter,
        query: String?
    ): List<Film> {
        val films = cacheDataSource.getPage(pageIndex, pageSize, query, filterBy)
        if (films.size == pageSize)
            return films
        val filmsResponse = networkDataSource.getFilms(pageIndex, pageSize, filterBy, query)
        return filmsResponse.docs.map(Doc::toFilm).apply {
            cacheDataSource.savePage(pageIndex, pageSize, this@apply, query, filterBy)
        }
    }

    override suspend fun isFavorite(filmId: Long): Boolean {
        return dbDataSource.contains(filmId)
    }

    override fun getFilms(filterBy: Filter, query: String?): Flow<PagingData<Film>> {
        val loader: FilmsLoader = { pageIndex, pageSize ->
            loadPage(pageIndex, pageSize, filterBy, query)
        }
        return createPager(loader)
    }

    override fun getFavoriteFilms(filterBy: Filter, query: String?): Flow<PagingData<Film>> {
        val loader: FilmsLoader = { pageIndex, pageSize ->
            dbDataSource.getPage(pageIndex, pageSize, filterBy, query)
        }
        return createPager(loader)
    }

    private fun createPager(loader: FilmsLoader): Flow<PagingData<Film>> {
        return Pager(
            config = PagingConfig(pageSize = PAGE_SIZE, initialLoadSize = PAGE_SIZE),
            pagingSourceFactory = {
                FilmsPagingSource(loader, PAGE_SIZE)
            }
        ).flow
    }


    override suspend fun addToFavorites(film: Film) {
        val filmDetails = getFilmDetails(filmId = film.id)
        addToFavorites(filmDetails)
    }

    override suspend fun addToFavorites(filmDetails: FilmDetails) {
        dbDataSource.add(filmDetails)
    }

    override suspend fun addToFavorites(filmId: Long) {
        addToFavorites(getFilmDetails(filmId))
    }

    override suspend fun removeFromFavorites(filmId: Long) {
        dbDataSource.remove(filmId)
    }

    override suspend fun removeAllFavorites() {
        dbDataSource.removeAll()
    }

    override suspend fun favoritesFilmsCount(): Int {
        return dbDataSource.count()
    }

    override suspend fun getFilmDetails(filmId: Long): FilmDetails {
        val details = dbDataSource.getDetails(filmId)
        if (details != null)
            return details
        val detailsResponse = networkDataSource.getFilmDetails(filmId)
        return detailsResponse.toFilmDetails()
    }
}