package com.pedroid.data.repository.playlist

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.pedroid.common.core.DataResource
import com.pedroid.data.dispatcher.BinDispatchers
import com.pedroid.data.dispatcher.Dispatcher
import com.pedroid.data.local.db.AppRoomDataBase
import com.pedroid.data.remote.api.playlists.PlaylistsApi
import com.pedroid.data.remote.api.playlists.dto.PlaylistRequestDto
import com.pedroid.data.remote.paging.SpotifyPlaylistRemoteMediator
import com.pedroid.domain.repository.PlaylistRepository
import com.pedroid.model.Playlist
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class PlaylistRepositoryImpl @Inject constructor(
    @Dispatcher(BinDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
    private val api: PlaylistsApi,
    private val db: AppRoomDataBase
) : PlaylistRepository {
    override fun getPlaylists(): Flow<PagingData<Playlist>> {
        val pagingSourceFactory = { db.playlistDao().pagingSource() }
        return Pager(
            config = PagingConfig(
                pageSize = 30,
                enablePlaceholders = false
            ),
            remoteMediator = SpotifyPlaylistRemoteMediator(api, db),
            pagingSourceFactory = pagingSourceFactory
        ).flow.map { pagingData ->
            pagingData.map { it.toDomain() }
        }
    }

    override suspend fun createPlaylist(userId: String, playlistName: String): DataResource<Unit> {
        return withContext(ioDispatcher) {
            try {
                api.createPlaylist(
                    userId = userId,
                    playlistRequest = PlaylistRequestDto(name = playlistName)
                )
                DataResource.Success(Unit)
            } catch (e: HttpException) {
                DataResource.Error(e)
            } catch (e: IOException) {
                DataResource.Error(e)
            }
        }
    }
}
