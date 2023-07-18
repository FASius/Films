package com.example.films.presentation

import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.films.R
import com.example.films.databinding.FragmentFilmDetailsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FilmDetailsFragment : Fragment(R.layout.fragment_film_details) {
    private val binding by viewBinding(FragmentFilmDetailsBinding::bind)

}