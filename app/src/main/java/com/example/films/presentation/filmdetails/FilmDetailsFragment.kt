package com.example.films.presentation.filmdetails

import android.os.Bundle
import android.view.View
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.example.films.R
import com.example.films.data.entities.FilmDetails
import com.example.films.databinding.FragmentFilmDetailsBinding
import dagger.hilt.android.AndroidEntryPoint

const val ARG_FILM_ID = "ARG_FILM_ID"

@AndroidEntryPoint
class FilmDetailsFragment : Fragment(R.layout.fragment_film_details) {
    private val viewModel by viewModels<FilmDetailsViewModel>()
    private val binding by viewBinding(FragmentFilmDetailsBinding::bind)
    private lateinit var adapter: ActorsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = ActorsAdapter()
        binding.actorsList.adapter = adapter
        binding.actorsList.layoutManager = LinearLayoutManager(requireContext())

        binding.tryAgain.setOnClickListener {
            viewModel.tryAgain()
        }

        viewModel.uiState.observe(viewLifecycleOwner) {
            processUiState(it)
        }
    }

    private fun processUiState(uiState: DetailsUiState) {
        when (uiState) {
            is DetailsUiState.Pending -> showProgress()
            is DetailsUiState.Error -> showError(uiState.e)
            is DetailsUiState.Success -> showDetails(uiState.data)
        }
    }
    private fun showError(e: Throwable) {
        setVisibilityForAllViews(View.GONE)
        binding.errorContainer.visibility = View.VISIBLE
        binding.errorText.text = e.message
    }

    private fun showProgress() {
        setVisibilityForAllViews(View.GONE)
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun showDetails(details: FilmDetails) {
        setVisibilityForAllViews(View.VISIBLE)
        with(binding) {
            progressBar.visibility = View.GONE
            errorContainer.visibility = View.GONE

            filmTitle.text = details.film.name
            filmDescription.text = details.description
            adapter.submitList(details.actors)
            Glide.with(requireContext()).load(details.film.posterUrl).into(filmPoster)
            filmRating.text = details.film.rating
        }
    }

    private fun setVisibilityForAllViews(visibility: Int) {
        binding.root.children.forEach {
            it.visibility = visibility
        }
    }

}