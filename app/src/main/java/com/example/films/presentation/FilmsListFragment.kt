package com.example.films.presentation

import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.films.R
import com.example.films.databinding.FragmentFilmsListBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FilmsListFragment : Fragment(R.layout.fragment_films_list) {

    private val binding by viewBinding(FragmentFilmsListBinding::bind)

}