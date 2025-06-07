package com.pedroid.testing.repository

import com.pedroid.data.remote.api.artists.ArtistsApi
import com.pedroid.data.remote.api.artists.dto.AlbumDto
import com.pedroid.data.remote.api.artists.dto.ArtistDto
import com.pedroid.data.remote.api.artists.dto.ArtistsDto
import com.pedroid.data.remote.api.artists.dto.AlbumsDto
import java.io.IOException

class FakeArtistsApi : ArtistsApi {

    private var artists: List<ArtistDto> = emptyList()
    private var albumsByArtist: Map<String, List<AlbumDto>> = emptyMap()
    private var shouldThrow: Boolean = false

    override suspend fun getArtists(offset: Int, limit: Int): ArtistsDto {
        if (shouldThrow) throw IOException("Simulated network error")
        val paginated = artists.drop(offset).take(limit)
        return ArtistsDto(items = paginated)
    }

    override suspend fun getArtistAlbumsById(
        idArtist: String,
        offset: Int,
        limit: Int
    ): AlbumsDto {
        if (shouldThrow) throw IOException("Simulated network error")
        val allAlbums = albumsByArtist[idArtist].orEmpty()
        val paginated = allAlbums.drop(offset).take(limit)
        return AlbumsDto(items = paginated)
    }

    fun setArtists(vararg artists: ArtistDto) {
        this.artists = artists.toList()
    }

    fun setAlbumsForArtist(artistId: String, vararg albums: AlbumDto) {
        this.albumsByArtist = this.albumsByArtist.toMutableMap().apply {
            put(artistId, albums.toList())
        }
    }

    fun simulateError(shouldThrow: Boolean) {
        this.shouldThrow = shouldThrow
    }
}
