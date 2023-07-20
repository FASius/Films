package com.example.films.presentation.filmslist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.example.films.data.films.FilmsRepository
import com.example.films.data.films.Filter
import com.example.films.data.films.entities.Film
import com.example.films.presentation.favoritefilms.FavoriteFilmsViewModel
import com.example.films.presentation.favoritefilms.FilmListItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FilmsListViewModel @Inject constructor(
    private val filmsRepository: FilmsRepository
) : FavoriteFilmsViewModel(filmsRepository) {

    private val _showFavorites = MutableLiveData(false)
    val showFavorites: LiveData<Boolean> = _showFavorites

    override fun getFilms(filterBy: Filter?, query: String?): Flow<PagingData<Film>> {
        return filmsRepository.getFilms(filterBy ?: Filter.RATING, query)
    }

    override fun addToFavorites(filmItem: FilmListItem) {
        viewModelScope.launch {
            filmsRepository.addToFavorites(filmItem.film)
            updateShowState()
        }
    }

    override fun removeFromFavorites(filmItem: FilmListItem) {
        viewModelScope.launch {
            filmsRepository.removeFromFavorites(filmItem.id)
            updateShowState()
        }
    }

    fun updateShowState() {
        viewModelScope.launch {
            _showFavorites.value = filmsRepository.favoritesFilmsCount() > 0
        }
    }
}