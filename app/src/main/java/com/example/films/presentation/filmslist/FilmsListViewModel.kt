package com.example.films.presentation.filmslist

import androidx.paging.PagingData
import com.example.films.data.films.FilmsRepository
import com.example.films.data.films.Filter
import com.example.films.data.films.entities.Film
import com.example.films.presentation.favoritefilms.FavoriteFilmsViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class FilmsListViewModel @Inject constructor(
    private val filmsRepository: FilmsRepository
) : FavoriteFilmsViewModel(filmsRepository) {

    override fun getFilms(filterBy: Filter?, query: String?): Flow<PagingData<Film>> {
        return filmsRepository.getFavoriteFilms(filterBy ?: Filter.RATING, query)
    }
}