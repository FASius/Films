package com.example.films.data.films.db.filmscache

import com.example.films.Constants
import com.example.films.data.films.Filter
import com.example.films.data.films.db.filmscache.entities.FilmCacheDbEntity
import com.example.films.data.films.entities.Film
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FilmsCacheDataSource @Inject constructor(
    private val filmsCacheDao: FilmsCacheDao
) {

    suspend fun getPage(pageIndex: Int, pageSize: Int, query: String?, filterBy: Filter): List<Film> {
        val filter = filterToString(filterBy)
        clearOld()
        val startOffset = pageIndex * pageSize
        val endOffset = startOffset + pageSize
        return filmsCacheDao.getPage(startOffset, endOffset, query, filter).map { it.toFilm() }
    }

    suspend fun savePage(pageIndex: Int, pageSize:Int, page: List<Film>, query: String?, filterBy: Filter) {
        val filter = filterToString(filterBy)
        clearOld()
        filmsCacheDao.insert(page.mapIndexed { index, film ->
            FilmCacheDbEntity.fromFilm(film, pageIndex * pageSize + index, query, filter)
        })
    }

    private suspend fun clearOld() {
        val before = System.currentTimeMillis() - Constants.CACHE_ENTITY_TTL_MS
        filmsCacheDao.clearAllBefore(before)
    }

    private fun filterToString(filterBy: Filter) = when (filterBy) {
        Filter.RATING -> "rating"
        Filter.RELEASE_DATE -> "release_date"
        Filter.VOTES -> "votes"
        Filter.ALPHABET -> "name"
    }

}