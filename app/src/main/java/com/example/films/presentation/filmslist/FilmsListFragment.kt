package com.example.films.presentation.filmslist

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.films.R
import com.example.films.databinding.FilmsListBinding
import com.example.films.databinding.FragmentFilmsListBinding
import com.example.films.presentation.favoritefilms.FavoritesFilmsFragment
import com.example.films.presentation.favoritefilms.FilmListItem
import com.example.films.presentation.filmdetails.ARG_FILM_ID
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FilmsListFragment : FavoritesFilmsFragment(R.layout.fragment_films_list) {
    override val viewModel: FilmsListViewModel by viewModels<FilmsListViewModel>()
    private val filmsBinding by viewBinding(FragmentFilmsListBinding::bind)
    override val binding: FilmsListBinding
        get() = filmsBinding.filmsLayout

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.showFavorites.observe(viewLifecycleOwner) { show ->
            filmsBinding.favoriteFilms.visibility = if (show) View.VISIBLE else View.GONE
        }
        viewModel.updateShowState()
    }

    override fun setupUi() {
        filmsBinding.favoriteFilms.setOnClickListener {
            findNavController().navigate(R.id.action_filmsListFragment_to_favoritesFilmsFragment)
        }
    }

    override fun onDetailsClick(filmItem: FilmListItem) {
        val args = bundleOf(ARG_FILM_ID to filmItem.id)
        findNavController().navigate(R.id.action_filmsListFragment_to_filmDetailsFragment, args)

    }
}