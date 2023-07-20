package com.example.films.presentation.filmdetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.films.data.films.FilmsRepository
import com.example.films.data.films.entities.FilmDetails
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class DetailsUiState {
    object Pending : DetailsUiState()
    class Error(val e: Throwable) : DetailsUiState()
    class Success(val data: FilmDetailsListItem) : DetailsUiState()
}

data class FilmDetailsListItem(
    val details: FilmDetails,
    val isFavorite: Boolean
) {
    val film get() = details.film
    val genres get() = details.genres
    val countries get() = details.countries
    val length get() = details.length
    val ageRating get() = details.ageRating
    val description get() = details.description
    val actors get() = details.actors
    val votes get() = details.votes
    val releaseDate get() = details.releaseDate
    val trailerUrl get() = details.trailerUrl
}

@HiltViewModel
class FilmDetailsViewModel @Inject constructor(
    private val filmsRepository: FilmsRepository,
    state: SavedStateHandle
) : ViewModel() {
    private val filmId: Long = state[ARG_FILM_ID]!!

    private val _uiState = MutableLiveData<DetailsUiState>(DetailsUiState.Pending)
    val uiState: LiveData<DetailsUiState> = _uiState

    init {
        loadDetails()
    }

    private fun loadDetails() {
        viewModelScope.launch {
            _uiState.value = try {
                val isFavorite = filmsRepository.isFavorite(filmId)
                val film = filmsRepository.getFilmDetails(filmId)
                DetailsUiState.Success(FilmDetailsListItem(film, isFavorite))
            } catch (e: Throwable) {
                DetailsUiState.Error(e)
            }
        }
    }

    fun tryAgain() {
        _uiState.value = DetailsUiState.Pending
        loadDetails()
    }

    fun addToFavorites() {
        viewModelScope.launch {
            filmsRepository.addToFavorites(filmId)
        }
    }

    fun removeFromFavorites() {
        viewModelScope.launch {
            filmsRepository.removeFromFavorites(filmId)
        }
    }

}