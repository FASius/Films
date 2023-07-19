package com.example.films.presentation.filmslist

import android.view.View
import android.widget.SearchView
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.films.R
import com.example.films.databinding.FragmentFavoritesFilmsBinding
import com.example.films.databinding.FragmentFilmsListBinding
import com.example.films.presentation.favoritefilms.FavoritesFilmsFragment
import com.example.films.presentation.filmdetails.ARG_FILM_ID
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
open class FilmsListFragment : FavoritesFilmsFragment(R.layout.fragment_films_list) {
    override val viewModel: FilmsListViewModel by viewModels<FilmsListViewModel>()
    private val favoritesBinding by viewBinding(FragmentFilmsListBinding::bind)
    override val binding: FragmentFavoritesFilmsBinding
        get() = favoritesBinding.filmsLayout

    override fun setupListeners(
        onQueryTextListener: SearchView.OnQueryTextListener,
        adapter: FilmsAdapter,
        layoutManager: RecyclerView.LayoutManager,
        filterClickListener: View.OnClickListener
    ) {
        super.setupListeners(onQueryTextListener, adapter, layoutManager, filterClickListener)
        favoritesBinding.favoriteFilms.setOnClickListener {
            findNavController().navigate(R.id.action_filmsListFragment_to_favoritesFilmsFragment)
        }
    }

    override fun onDetailsClick(filmsId: Long) {
        val args = bundleOf(ARG_FILM_ID to filmsId)
        findNavController().navigate(R.id.action_filmsListFragment_to_filmDetailsFragment, args)

    }
}