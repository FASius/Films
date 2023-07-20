package com.example.films.presentation.filmslist

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.core.view.children
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.films.databinding.FilmItemBinding
import com.example.films.presentation.favoritefilms.FilmListItem

class FilmsAdapter(
    private val onFavoriteClick: (FilmListItem, Boolean) -> Unit,
    private val onItemClick: (FilmListItem) -> Unit
) : PagingDataAdapter<FilmListItem, FilmsAdapter.FilmsViewHolder>(FilmsDiffCallback()) {

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
        private val onFavoriteClick: (FilmListItem, Boolean) -> Unit,
        private val onItemClick: (FilmListItem) -> Unit
    ) : ViewHolder(binding.root) {
        fun bind(film: FilmListItem?) {
            if (film == null)
                return
            with(binding) {
                root.tag = film
                root.setOnClickListener {
                    onItemClick(it.tag as FilmListItem)
                }
                root.children.forEach {
                    it.tag = film
                }
                addToFavorite.isChecked = film.isFavorite
                filmName.text = film.name
                filmRating.text = String.format("%.1f", film.rating)
                addToFavorite.tag = film
                addToFavorite.setOnClickListener {
                    val checked = (it as CheckBox).isChecked
                    onFavoriteClick(it.tag as FilmListItem, checked)
                }
                Glide.with(filmPoster)
                    .load(film.posterUrl)
                    .into(filmPoster)
            }
        }
    }
}

class FilmsDiffCallback: DiffUtil.ItemCallback<FilmListItem>() {
    override fun areItemsTheSame(oldItem: FilmListItem, newItem: FilmListItem): Boolean {
        return oldItem.id == newItem.id && oldItem.isFavorite == newItem.isFavorite
    }

    override fun areContentsTheSame(oldItem: FilmListItem, newItem: FilmListItem): Boolean {
        return oldItem == newItem
    }
}
