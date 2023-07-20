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
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.films.R
import com.example.films.data.films.Filter
import com.example.films.databinding.FilmsListBinding
import com.example.films.databinding.FragmentFavoritesFilmsBinding
import com.example.films.presentation.filmdetails.ARG_FILM_ID
import com.example.films.presentation.filmslist.FilmsAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch

@AndroidEntryPoint
open class FavoritesFilmsFragment(@LayoutRes layoutRes: Int) : Fragment(layoutRes) {
    constructor() : this(R.layout.fragment_favorites_films)

    private val favoritesBinding by viewBinding(FragmentFavoritesFilmsBinding::bind)
    protected open val binding: FilmsListBinding
        get() = favoritesBinding.filmsLayout
    protected open val viewModel by viewModels<FavoriteFilmsViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = createAdapter()
        val layoutManager = LinearLayoutManager(requireContext())
        val adapterWithFooter = adapter.withLoadStateFooter(DefaultLoadStateAdapter())
        viewLifecycleOwner.lifecycleScope.launch {
            adapter.loadStateFlow.debounce(200).collectLatest {
                if (it.refresh is LoadState.Loading) {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.filmList.visibility = View.INVISIBLE
                }
                if (it.refresh is LoadState.NotLoading) {
                    binding.progressBar.visibility = View.GONE
                    binding.filmList.visibility = View.VISIBLE
                }
            }
        }
        setupUi(onQueryChangeListener, adapterWithFooter, layoutManager, ::showDialog)

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.films.collect {
                adapter.submitData(it)
            }
        }
    }

    private fun setupUi(
        onQueryTextListener: SearchView.OnQueryTextListener,
        adapter: RecyclerView.Adapter<*>,
        layoutManager: RecyclerView.LayoutManager,
        filterClickListener: View.OnClickListener
    ) {
        binding.searchView.setOnQueryTextListener(onQueryTextListener)
        binding.filmList.adapter = adapter
        binding.filmList.layoutManager = layoutManager
        binding.filter.setOnClickListener(filterClickListener)
        setupUi()
    }

    protected open fun setupUi() {
        favoritesBinding.clear.setOnClickListener {
            viewModel.clearAll()
        }
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

    protected open fun onDetailsClick(filmItem: FilmListItem) {
        val args = bundleOf(ARG_FILM_ID to filmItem.id)
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
            getString(R.string.filter_name) -> Filter.ALPHABET
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
                    Filter.ALPHABET -> R.string.filter_name
                }
            }
    }
}
