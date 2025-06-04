package com.pedroid.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.pedroid.feature.home.databinding.ArtistItemBinding
import com.pedroid.model.Artist

class ArtistAdapter : PagingDataAdapter<Artist, ArtistAdapter.ArtistViewHolder>(DIFF) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtistViewHolder {
        val binding = ArtistItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ArtistViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ArtistViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item)
        }
    }

    inner class ArtistViewHolder(
        private val binding: ArtistItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Artist) = with(binding) {
            name.text = item.name
        }
    }

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<Artist>() {
            override fun areItemsTheSame(oldItem: Artist, newItem: Artist): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Artist, newItem: Artist): Boolean {
                return oldItem == newItem
            }
        }
    }
}
