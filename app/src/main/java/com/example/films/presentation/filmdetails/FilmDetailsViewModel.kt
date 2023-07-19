package com.example.films.presentation.filmdetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.films.data.FilmsRepository
import com.example.films.data.entities.FilmDetails
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class DetailsUiState {
    object Pending : DetailsUiState()
    class Error(val e: Throwable) : DetailsUiState()
    class Success(val data: FilmDetails) : DetailsUiState()
}

@HiltViewModel
class FilmDetailsViewModel @Inject constructor(
    private val filmsRepository: FilmsRepository,
    state: SavedStateHandle
) : ViewModel() {
    private val filmId: Long? = state[ARG_FILM_ID]

    private val _uiState = MutableLiveData<DetailsUiState>(DetailsUiState.Pending)
    val uiState: LiveData<DetailsUiState> = _uiState

    init {
        loadDetails()
    }

    private fun loadDetails() {
        viewModelScope.launch {
            _uiState.value = try {
                DetailsUiState.Success(filmsRepository.getFilmDetails(filmId!!))
            } catch (e: Throwable) {
                DetailsUiState.Error(e)
            }
        }
    }

    fun tryAgain() {
        _uiState.value = DetailsUiState.Pending
        loadDetails()
    }

}