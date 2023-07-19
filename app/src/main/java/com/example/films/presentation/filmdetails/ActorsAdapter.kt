package com.example.films.presentation.filmdetails

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.films.data.films.entities.Actor
import com.example.films.databinding.ActorItemBinding

class ActorsAdapter : ListAdapter<Actor, ActorsAdapter.ActorsViewHolder>(ActorsDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActorsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ActorItemBinding.inflate(inflater, parent, false)
        return ActorsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ActorsViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ActorsViewHolder(
        private val binding: ActorItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(actor: Actor) {
            with(binding) {
                actorRealName.text = actor.realName
                actorCharacterName.text = actor.characterName
                Glide.with(root)
                    .load(actor.imageUrl)
                    .into(actorPhoto)
            }
        }
    }
}

class ActorsDiffCallback : DiffUtil.ItemCallback<Actor>() {
    override fun areItemsTheSame(oldItem: Actor, newItem: Actor): Boolean {
        return oldItem.realName == newItem.realName
    }

    override fun areContentsTheSame(oldItem: Actor, newItem: Actor): Boolean {
        return oldItem == newItem
    }
}