package com.pedroid.privatemodule

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.pedroid.common.ext.loadImage
import com.pedroid.feature.albums.privatemodule.databinding.AlbumItemBinding
import com.pedroid.model.Album

class AlbumsAdapter : PagingDataAdapter<Album, AlbumsAdapter.PlaylistViewHolder>(DIFF) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val binding = AlbumItemBinding.inflate(
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
        private val binding: AlbumItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Album) = with(binding) {
            name.text = item.name
            date.text = item.releaseDate
            image.loadImage(url = item.imageUrl)
        }
    }

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<Album>() {
            override fun areItemsTheSame(oldItem: Album, newItem: Album): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Album, newItem: Album): Boolean {
                return oldItem == newItem
            }
        }
    }
}
