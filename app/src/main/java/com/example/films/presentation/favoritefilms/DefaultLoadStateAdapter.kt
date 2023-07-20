package com.example.films.presentation.favoritefilms

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.films.databinding.LoadingFilmsBinding

class DefaultLoadStateAdapter : LoadStateAdapter<DefaultLoadStateAdapter.Holder>() {
    override fun onBindViewHolder(holder: Holder, loadState: LoadState) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): Holder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = LoadingFilmsBinding.inflate(inflater, parent, false)
        return Holder(binding)
    }

    class Holder(
        binding: LoadingFilmsBinding
    ) : RecyclerView.ViewHolder(binding.root)
}