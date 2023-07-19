package com.example.films.presentation.filmslist

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.core.view.children
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.films.data.entities.Film
import com.example.films.databinding.FilmItemBinding

class FilmsAdapter(
    private val onFavoriteClick: (Film, Boolean) -> Unit,
    private val onItemClick: (Long) -> Unit
) : PagingDataAdapter<Film, FilmsAdapter.FilmsViewHolder>(FilmsDiffCallback()) {

    override fun onBindViewHolder(holder: FilmsViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilmsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = FilmItemBinding.inflate(inflater, parent, false)
        return FilmsViewHolder(binding, onFavoriteClick, onItemClick)
    }

    class FilmsViewHolder(
        private val binding: FilmItemBinding,
        private val onFavoriteClick: (Film, Boolean) -> Unit,
        private val onItemClick: (Long) -> Unit
    ) : ViewHolder(binding.root) {
        fun bind(film: Film?) {
            if (film == null)
                return
            with(binding) {
                root.tag = film.id
                root.setOnClickListener {
                    onItemClick(it.tag as Long)
                }
                root.children.forEach {
                    it.tag = film
                }
                addToFavorite.isChecked = film.isFavorite
                filmName.text = film.name
                filmRating.text = film.rating
                addToFavorite.tag = film
                addToFavorite.setOnClickListener {
                    val checked = (it as CheckBox).isChecked
                    onFavoriteClick(it.tag as Film, checked)
                }
                Glide.with(filmPoster)
                    .load(film.posterUrl)
                    .into(filmPoster)
            }
        }
    }
}

class FilmsDiffCallback: DiffUtil.ItemCallback<Film>() {
    override fun areItemsTheSame(oldItem: Film, newItem: Film): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Film, newItem: Film): Boolean {
        return oldItem == newItem
    }
}
