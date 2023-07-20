package com.example.films.data.films.db.favirites

import com.example.films.data.films.Filter
import com.example.films.data.films.db.favirites.entities.FavoriteFilmTuple
import com.example.films.data.films.db.favirites.entities.FavoriteFilmsDbEntity
import com.example.films.data.films.entities.Film
import com.example.films.data.films.entities.FilmDetails
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FavoriteFilmsDataSource @Inject constructor(
    private val favoritesDao: FavoritesDao
) {

    suspend fun getPage(
        pageIndex: Int,
        pageSize: Int,
        filterBy: Filter,
        query: String?
    ): List<Film> {
        val filter = filterToString(filterBy)
        val offset = pageSize * pageIndex
        val entity = if (query == null)
            favoritesDao.getAll(pageSize, offset, filter)
        else
            favoritesDao.getAll(pageSize, offset, filter, "%$query%")
        return entity.map(FavoriteFilmTuple::toFilm)
    }

    suspend fun getDetails(filmId: Long): FilmDetails? {
        return favoritesDao.getDetails(filmId)?.toFilmDetails()
    }

    suspend fun add(filmDetails: FilmDetails) {
        val entity = FavoriteFilmsDbEntity.fromFilmDetails(filmDetails)
        favoritesDao.insert(entity)
    }

    suspend fun remove(id: Long) {
        favoritesDao.remove(id)
    }

    suspend fun removeAll() {
        favoritesDao.removeAll()
    }

    suspend fun contains(id: Long): Boolean {
        return favoritesDao.countById(id) > 0
    }

    suspend fun count(): Int {
        return favoritesDao.count()
    }

    private fun filterToString(filterBy: Filter) = when (filterBy) {
        Filter.RATING -> "rating"
        Filter.RELEASE_DATE -> "release_date"
        Filter.VOTES -> "votes"
        Filter.ALPHABET -> "name"
    }

}