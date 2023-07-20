package com.example.films.data.films

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.films.data.films.entities.Film

typealias FilmsLoader = suspend (pageIndex: Int, pageSize: Int) -> List<Film>

class FilmsPagingSource(
    private val loader: FilmsLoader,
    private val pageSize: Int
) : PagingSource<Int, Film>() {
    override fun getRefreshKey(state: PagingState<Int, Film>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        val anchorPage = state.closestPageToPosition(anchorPosition) ?: return null
        return anchorPage.nextKey?.minus(1) ?: anchorPage.prevKey?.plus(1)
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Film> {
        val pageIndex = params.key ?: 0
        return try {
            val films = loader(pageIndex, params.loadSize)
            LoadResult.Page(
                data = films,
                prevKey = if (pageIndex == 0) null else pageIndex - 1,
                nextKey = if (params.loadSize == films.size) pageIndex + params.loadSize / pageSize else null
            )
        } catch (e: Throwable) {
            LoadResult.Error(e)
        }
    }
}