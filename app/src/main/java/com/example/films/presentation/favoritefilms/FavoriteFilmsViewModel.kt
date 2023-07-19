package com.example.films.presentation.favoritefilms

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.films.core.ui.combineWith
import com.example.films.data.films.FilmsRepository
import com.example.films.data.films.Filter
import com.example.films.data.films.entities.Film
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class ResponseParameters(
    val filterBy: Filter?,
    val query: String?
)

@HiltViewModel
open class FavoriteFilmsViewModel @Inject constructor(
    private val filmsRepository: FilmsRepository
) : ViewModel() {

    private val query = MutableLiveData<String>(null)
    private val filterBy = MutableLiveData<Filter>(null)
    var films: Flow<PagingData<Film>> = query.combineWith(filterBy) { query, filterBy ->
        ResponseParameters(filterBy, query)
    }.asFlow()
        .flatMapLatest {
            getFilms(it.filterBy, it.query)
        }
        .debounce(500)
        .cachedIn(viewModelScope)


    fun search(query: String) {
        if (this.query.value != query) {
            this.query.value = query
        }
    }

    fun filterBy(by: Filter) {
        if (this.filterBy.value != by) {
            this.filterBy.value = by
        }
    }

    fun addToFavorites(film: Film) {
        viewModelScope.launch {
            filmsRepository.addToFavorites(film)
        }
    }

    fun removeFromFavorites(film: Film) {
        viewModelScope.launch {
            filmsRepository.removeFromFavorites(film)
        }
    }

    protected open fun getFilms(filterBy: Filter?, query: String?): Flow<PagingData<Film>> {
        return filmsRepository.getFavoriteFilms(filterBy ?: Filter.RATING, query)
    }

}