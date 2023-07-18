package com.example.films.presentation

import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.films.R
import com.example.films.databinding.FragmentFavoritesFilmsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoritesFilmsFragment : Fragment(R.layout.fragment_favorites_films) {
    private val binding by viewBinding(FragmentFavoritesFilmsBinding::bind)

}