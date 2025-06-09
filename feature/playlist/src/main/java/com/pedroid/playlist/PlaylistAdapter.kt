package com.pedroid.playlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.pedroid.common.extension.loadImage
import com.pedroid.feature.playlist.R
import com.pedroid.feature.playlist.databinding.PlaylistItemBinding
import com.pedroid.model.Playlist

class PlaylistAdapter : PagingDataAdapter<Playlist, PlaylistAdapter.PlaylistViewHolder>(DIFF) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val binding = PlaylistItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PlaylistViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item)
        }
    }

    inner class PlaylistViewHolder(
        private val binding: PlaylistItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Playlist) = with(binding) {
            name.text = item.name
            description.text =
                item.description?.takeIf { it.isNotEmpty() } ?: binding.root.context.getString(
                    R.string.without_description
                )
            image.loadImage(url = item.imageUrl, error = R.drawable.ic_image_playlist)
        }
    }

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<Playlist>() {
            override fun areItemsTheSame(oldItem: Playlist, newItem: Playlist): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Playlist, newItem: Playlist): Boolean {
                return oldItem == newItem
            }
        }
    }
}
