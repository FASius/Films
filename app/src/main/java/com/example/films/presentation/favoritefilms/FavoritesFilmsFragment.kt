package com.example.films.presentation.favoritefilms

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.widget.SearchView
import androidx.annotation.LayoutRes
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.films.R
import com.example.films.data.Filter
import com.example.films.databinding.FragmentFavoritesFilmsBinding
import com.example.films.presentation.filmdetails.ARG_FILM_ID
import com.example.films.presentation.filmslist.FilmsAdapter
import com.example.films.presentation.filmslist.FilmsListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
open class FavoritesFilmsFragment(@LayoutRes layoutRes: Int) : Fragment(layoutRes) {
    constructor() : this(R.layout.fragment_favorites_films)

    protected open val binding by viewBinding(FragmentFavoritesFilmsBinding::bind)
    protected open val viewModel by viewModels<FilmsListViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = createAdapter()
        val layoutManager = LinearLayoutManager(requireContext())
        setupListeners(onQueryChangeListener, adapter, layoutManager, ::showDialog)

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.films.collect {
                adapter.submitData(it)
                println(adapter)
                println(it)
            }
        }
    }

    protected open fun setupListeners(
        onQueryTextListener: SearchView.OnQueryTextListener,
        adapter: FilmsAdapter,
        layoutManager: RecyclerView.LayoutManager,
        filterClickListener: View.OnClickListener
    ) {
        binding.searchView.setOnQueryTextListener(onQueryTextListener)
        binding.filmList.adapter = adapter
        binding.filmList.layoutManager = layoutManager
        binding.filter.setOnClickListener(filterClickListener)
    }

    private var currentIndex = 0
    private fun showDialog(view: View) {
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle(R.string.filter_dialog_title)
            .setSingleChoiceItems(
                getFilters(),
                currentIndex
            ) { _, which ->
                currentIndex = which
            }
            .setPositiveButton(R.string.filter_dialog_apply) { _, _ ->
                viewModel.filterBy(getFilterByString(getFilters()[currentIndex]))
            }.create()
        dialog.show()
    }

    private fun createAdapter() = FilmsAdapter({ film, isChecked ->
        if (isChecked)
            viewModel.addToFavorites(film)
        else
            viewModel.removeFromFavorites(film)
    }, ::onDetailsClick)

    protected open fun onDetailsClick(filmsId: Long) {
        val args = bundleOf(ARG_FILM_ID to filmsId)
        findNavController().navigate(
            R.id.action_favoritesFilmsFragment_to_filmDetailsFragment,
            args
        )
    }

    private val onQueryChangeListener = object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            return true
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            if (newText != null) {
                viewModel.search(newText)
            }
            return true
        }
    }

    private fun getFilters() = filtersStringIds.map {
        getString(it)
    }.toTypedArray()

    private fun getFilterByString(value: String): Filter {
        return when(value) {
            getString(R.string.filter_rating) -> Filter.RATING
            getString(R.string.filter_votes) -> Filter.VOTES
            getString(R.string.filter_date) -> Filter.RELEASE_DATE
            else -> throw IllegalStateException()
        }
    }

    companion object {
        private val filtersStringIds: List<Int>
            get() = Filter.values().map {
                when (it) {
                    Filter.RATING -> R.string.filter_rating
                    Filter.VOTES -> R.string.filter_votes
                    Filter.RELEASE_DATE -> R.string.filter_date
                }
            }
    }
}