package com.pedroid.data.remote.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.pedroid.data.local.AppRoomDataBase
import com.pedroid.data.local.artists.entity.ArtistEntity
import com.pedroid.data.local.artists.entity.ArtistRemoteKeys
import com.pedroid.data.local.playlists.entity.PlaylistEntity
import com.pedroid.data.local.playlists.entity.PlaylistRemoteKeys
import com.pedroid.data.remote.playlists.PlaylistsApi
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class SpotifyPlaylistRemoteMediator(
    private val api: PlaylistsApi, private val db: AppRoomDataBase
) : RemoteMediator<Int, PlaylistEntity>() {

    private val playlistDao = db.playlistDao()
    private val remoteKeysDao = db.playlistRemoteKeysDao()

    override suspend fun load(
        loadType: LoadType, state: PagingState<Int, PlaylistEntity>
    ): MediatorResult {
        val offset = when (loadType) {
            LoadType.REFRESH -> 0
            LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
            LoadType.APPEND -> {
                val lastItem = state.lastItemOrNull() ?: return MediatorResult.Success(true)
                val remoteKey =
                    remoteKeysDao.remoteKeysById(lastItem.id) ?: return MediatorResult.Success(true)
                remoteKey.nextKey ?: return MediatorResult.Success(true)
            }
        }

        try {
            val response = api.getPlaylists(limit = state.config.pageSize, offset = offset)
            val playlists = response.items.map { dto ->
                PlaylistEntity(
                    id = dto.id,
                    imageUrl = dto.images?.firstOrNull()?.url,
                    name = dto.name,
                    description = dto.description,
                )
            }

            val endReached = playlists.isEmpty()

            db.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    playlistDao.clearAll()
                    remoteKeysDao.clearRemoteKeys()
                }

                playlistDao.upsertPlaylists(playlists)

                val keys = playlists.map { playlist ->
                    PlaylistRemoteKeys(
                        playlistId = playlist.id,
                        prevKey = if (offset == 0) null else offset - state.config.pageSize,
                        nextKey = if (endReached) null else offset + state.config.pageSize
                    )
                }

                remoteKeysDao.insertAll(keys)
            }

            return MediatorResult.Success(endOfPaginationReached = endReached)
        } catch (e: IOException) {
            return MediatorResult.Error(e)
        } catch (e: HttpException) {
            return MediatorResult.Error(e)
        }
    }
}
