package com.example.films.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.films.data.entities.Actor
import com.example.films.data.entities.Film
import com.example.films.data.entities.FilmDetails
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import java.util.Date
import javax.inject.Inject
import kotlin.random.Random

class FilmsRepositoryImpl @Inject constructor() : FilmsRepository {

    override suspend fun loadFilms() {
        delay(1000)
        // TODO: implement
    }

    override fun getFilms(filterBy: Filter, query: String?): Flow<PagingData<Film>> {
        // TODO: implement
        return Pager(
            config = PagingConfig(
                PAGE_SIZE,
                enablePlaceholders = true,
                initialLoadSize = 20
            ),
            pagingSourceFactory = {
                object : PagingSource<Int, Film>() {
                    override fun getRefreshKey(state: PagingState<Int, Film>): Int? {
                        val anchorPosition = state.anchorPosition ?: return null
                        val anchorPage = state.closestPageToPosition(anchorPosition) ?: return null
                        return anchorPage.nextKey?.minus(1) ?: anchorPage.prevKey?.plus(1)
                    }

                    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Film> {
                        val pageIndex = params.key ?: 0
                        var filmsList = if (query != null) {
                            FILM_LIST.filter {
                                it.name.contains(query, true)
                            }
                        } else {
                            FILM_LIST
                        }
                        filmsList = when (filterBy) {
                                Filter.VOTES -> filmsList.sortedByDescending { it.rating }
                                Filter.RATING -> filmsList.sortedByDescending { it.rating }
                                Filter.RELEASE_DATE -> filmsList.sortedByDescending { it.year }
                            }
                        return LoadResult.Page(
                            data = filmsList,
                            prevKey = if (pageIndex == 0) null else pageIndex - 1,
                            nextKey =
                            if (FILM_LIST.size == params.loadSize) pageIndex + (params.loadSize / PAGE_SIZE) else null
                        )
                    }
                }
            }).flow
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
        delay(1500)
        if (Random.nextBoolean()) {
            throw IllegalStateException("Unable to load details. Please try again")
        }
        return FilmDetails(
            FILM_LIST[0],
            listOf("Crime", "Drama", "Fantasy"),
            listOf("USA"),
            189,
            16,
            "Пол Эджкомб — начальник блока смертников в тюрьме «Холодная гора», каждый из узников которого однажды проходит «зеленую милю» по пути к месту казни. Пол повидал много заключённых и надзирателей за время работы. Однако гигант Джон Коффи, обвинённый в страшном преступлении, стал одним из самых необычных обитателей блока.",
            listOf(
                Actor(
                    "Том Хэнкс",
                    "https://www.kinopoisk.ru/images/sm_actor/9144.jpg",
                    "Paul Edgecomb"
                ),
                Actor(
                    "Дэвид Морс",
                    "https://www.kinopoisk.ru/images/sm_actor/12759.jpg",
                    "Brutus «Brutal» Howell"
                )
            ),
            908250,
            Date(1999, 11, 6),
            "https://www.kinopoisk.ru/film/435/video/13494/"
        )
    }

    companion object {
        private const val PAGE_SIZE = 20
        private val FILM_LIST = listOf(
            Film(
                false,
                1,
                "The Green Mile",
                "https://st.kp.yandex.net/images/film_big/435.jpg",
                "9.2",
                1999
            ),
            Film(
                true,
                2,
                "Джентельмены",
                "https://st.kp.yandex.net/images/film_iphone/iphone360_1143242.jpg",
                "7.8",
                2019
            )
        )
    }
}