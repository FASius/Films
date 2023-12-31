package com.example.films.presentation.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.films.data.films.FilmsRepository
import com.example.films.data.films.Filter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val filmsRepository: FilmsRepository
) : ViewModel() {

    private val _isLoading = MutableLiveData(true)
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        viewModelScope.launch {
            val startLoadingTime = System.currentTimeMillis()
            try {
                filmsRepository.loadFilms(Filter.RATING)
            } catch (_: Throwable) {}
            val endLoadingTime = System.currentTimeMillis()
            val loadingTime = endLoadingTime - startLoadingTime
            if (loadingTime < MIN_SCREEN_LOAD_TIME_MS) {
                delay(MIN_SCREEN_LOAD_TIME_MS - loadingTime)
            }
            _isLoading.value = false
        }
    }

    companion object {
        private const val MIN_SCREEN_LOAD_TIME_MS = 3000
    }

}