package com.example.films.data.films

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.films.data.films.entities.Film

class FilmsPage(
    val films: List<Film>,
    val currentPage: Int,
    val pagesCount: Int
)
typealias FilmsLoader = suspend (pageIndex: Int, pageSize: Int) -> FilmsPage

class FilmsPagingSource(
    private val loader: FilmsLoader
) : PagingSource<Int, Film>() {
    override fun getRefreshKey(state: PagingState<Int, Film>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        val anchorPage = state.closestPageToPosition(anchorPosition) ?: return null
        return anchorPage.nextKey?.minus(1) ?: anchorPage.prevKey?.plus(1)
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Film> {
        val pageIndex = params.key ?: 0
        return try {
            val page = loader(pageIndex, params.loadSize)
            LoadResult.Page(
                data = page.films,
                prevKey = if (pageIndex == 0) null else pageIndex - 1,
                nextKey = if (page.currentPage < page.pagesCount) pageIndex + 1 else null
            )
        } catch (e: Throwable) {
            LoadResult.Error(e)
        }
    }
}