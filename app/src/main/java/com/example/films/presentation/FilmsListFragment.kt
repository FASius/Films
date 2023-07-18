package com.example.films.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.films.R
import com.example.films.databinding.FragmentFilmsListBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FilmsListFragment : Fragment(R.layout.fragment_films_list) {

    private val binding by viewBinding(FragmentFilmsListBinding::bind)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }
}