package com.example.films.presentation.filmdetails

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.example.films.R
import com.example.films.databinding.FragmentFilmDetailsBinding
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Date

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
        binding.addToFavorite.setOnClickListener {
            if ((it as CheckBox).isChecked)
                viewModel.addToFavorites()
            else
                viewModel.removeFromFavorites()
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

    private fun showDetails(details: FilmDetailsListItem) {
        setVisibilityForAllViews(View.VISIBLE)
        with(binding) {
            progressBar.visibility = View.GONE
            errorContainer.visibility = View.GONE
            if (details.trailerUrl != null) {
                binding.trailer.setOnClickListener {
                    it.isEnabled = false
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(details.trailerUrl))
                    startActivity(intent)
                    it.isEnabled = true
                }
            }
            else
                binding.trailer.isEnabled = false
            filmMetadata.text = formatMetadata(details)
            addToFavorite.isChecked = details.isFavorite
            filmTitle.text = details.film.name
            filmDescription.text = details.description
            adapter.submitList(details.actors)
            Glide.with(requireContext()).load(details.film.posterUrl).into(filmPoster)
            filmRating.text = String.format("%.1f", details.film.rating)
        }
    }

    private fun formatMetadata(details: FilmDetailsListItem): String {
        val countries = details.countries.joinToString()
        val genres = details.genres.joinToString()
        val length = if (details.length != null) "${details.length} minutes" else ""
        val ageRating = details.ageRating ?: ""
        val releaseDate = if (details.releaseDate != null) formatDate(details.releaseDate!!) else ""
        return "$countries $genres\n$length $ageRating $releaseDate"
    }

    private fun formatDate(date: Date): String {
        val formatter = SimpleDateFormat("dd MMM yyyy")
        return formatter.format(date)
    }

    private fun setVisibilityForAllViews(visibility: Int) {
        binding.root.children.forEach {
            it.visibility = visibility
        }
    }

}