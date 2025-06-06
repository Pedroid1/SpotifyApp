package com.pedroid.data.repository.artists

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.pedroid.data.local.db.AppRoomDataBase
import com.pedroid.data.remote.api.artists.ArtistsApi
import com.pedroid.data.remote.paging.SpotifyAlbumsRemoteMediator
import com.pedroid.data.remote.paging.SpotifyArtistRemoteMediator
import com.pedroid.domain.repository.ArtistsRepository
import com.pedroid.model.Album
import com.pedroid.model.Artist
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class ArtistsRepositoryImpl @Inject constructor(
    private val api: ArtistsApi,
    private val db: AppRoomDataBase
) : ArtistsRepository {
    override fun getArtists(): Flow<PagingData<Artist>> {
        val pagingSourceFactory = { db.artistDao().pagingSource() }
        return Pager(
            config = PagingConfig(
                pageSize = 30,
                enablePlaceholders = false
            ),
            remoteMediator = SpotifyArtistRemoteMediator(api, db),
            pagingSourceFactory = pagingSourceFactory
        ).flow.map { pagingData ->
            pagingData.map { it.toDomain() }
        }
    }

    override fun getArtistAlbumsById(id: String): Flow<PagingData<Album>> {
        val pagingSourceFactory = { db.albumsDao().pagingSource(artistId = id) }
        return Pager(
            config = PagingConfig(
                pageSize = 30,
                enablePlaceholders = false
            ),
            remoteMediator = SpotifyAlbumsRemoteMediator(api, db, id),
            pagingSourceFactory = pagingSourceFactory
        ).flow.map { pagingData ->
            pagingData.map { it.toDomain() }
        }
    }
}
