package com.example.films.presentation.favoritefilms

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.films.core.ui.combineWith
import com.example.films.data.films.FilmsRepository
import com.example.films.data.films.Filter
import com.example.films.data.films.entities.Film
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

data class FilmListItem(
    val film: Film,
    var isFavorite: Boolean
) {
    val id get() = film.id
    val name get() = film.name
    val posterUrl get() = film.posterUrl
    val rating get() = film.rating
    val year get() = film.year
}

@HiltViewModel
open class FavoriteFilmsViewModel @Inject constructor(
    private val filmsRepository: FilmsRepository
) : ViewModel() {

    private val query = MutableLiveData<String>(null)
    private val filterBy = MutableLiveData<Filter>(null)
    private val localChanges = LocalChanges()
    private val localChangesFlow = MutableStateFlow(localChanges)
    var films: Flow<PagingData<FilmListItem>> = query.combineWith(filterBy) { query, filterBy ->
        ResponseParameters(filterBy, query)
    }.asFlow()
        .flatMapLatest {
            getFilms(it.filterBy, it.query)
        }
        .combine(localChangesFlow) { pagingData, localChanges ->
            pagingData.map {
                val isFavorite = localChanges.favoritesFlags[it.id]
                if (isFavorite != null) {
                    Log.d("FavoriteFilmsViewModel",  localChanges.favoritesFlags.toString())
                    FilmListItem(it, isFavorite)
                }
                else
                    FilmListItem(it, filmsRepository.isFavorite(it.id))
            }
        }
        .debounce(500)
        .cachedIn(viewModelScope)

    fun search(query: String) {
        if (this.query.value != query) {
            this.query.value = if (query == "") null else query
        }
    }

    fun filterBy(by: Filter) {
        if (this.filterBy.value != by) {
            this.filterBy.value = by
        }
    }

    open fun addToFavorites(filmItem: FilmListItem) {
        viewModelScope.launch {
            filmsRepository.addToFavorites(filmItem.film)
            localChanges.favoritesFlags[filmItem.id] = true
            filmItem.isFavorite = true
            localChangesFlow.value = localChangesFlow.value.copy()
        }
    }

    open fun removeFromFavorites(filmItem: FilmListItem) {
        viewModelScope.launch {
            filmsRepository.removeFromFavorites(filmItem.id)
            localChanges.favoritesFlags[filmItem.id] = false
            filmItem.isFavorite = false
            localChangesFlow.value = localChangesFlow.value.copy()
        }
    }

    fun clearAll() {
        viewModelScope.launch {
            filmsRepository.removeAllFavorites()
            query.value = query.value
        }
    }

    protected open fun getFilms(filterBy: Filter?, query: String?): Flow<PagingData<Film>> {
        return filmsRepository.getFavoriteFilms(filterBy ?: Filter.RATING, query)
    }

    private data class LocalChanges(
        val favoritesFlags: MutableMap<Long, Boolean> = mutableMapOf()
    )

    private class ResponseParameters(
        val filterBy: Filter?,
        val query: String?
    )
}