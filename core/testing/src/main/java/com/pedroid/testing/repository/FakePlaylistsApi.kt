package com.pedroid.testing.repository

import com.pedroid.data.remote.api.playlists.PlaylistsApi
import com.pedroid.data.remote.api.playlists.dto.PlaylistDto
import com.pedroid.data.remote.api.playlists.dto.PlaylistRequestDto
import com.pedroid.data.remote.api.playlists.dto.PlaylistResponseDto
import java.io.IOException

class FakePlaylistsApi : PlaylistsApi {

    private var playlists: MutableList<PlaylistDto> = mutableListOf()
    private var shouldThrow: Boolean = false

    override suspend fun getPlaylists(offset: Int, limit: Int): PlaylistResponseDto {
        if (shouldThrow) throw IOException("Simulated network error")
        val paginated = playlists.drop(offset).take(limit)
        return PlaylistResponseDto(items = paginated)
    }

    override suspend fun createPlaylist(userId: String, playlistRequest: PlaylistRequestDto) {
        if (shouldThrow) throw IOException("Simulated network error")
        playlists.add(
            PlaylistDto(
                id = "playlist_${playlists.size + 1}",
                name = playlistRequest.name,
                description = "",
                images = emptyList()
            )
        )
    }

    fun setPlaylists(playlists: List<PlaylistDto>) {
        this.playlists = playlists.toMutableList()
    }

    fun clearPlaylists() {
        playlists.clear()
    }

    fun throwOnRequest(shouldFail: Boolean) {
        this.shouldThrow = shouldFail
    }
}
