package com.pedroid.data.remote.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.pedroid.data.local.db.AppRoomDataBase
import com.pedroid.data.local.db.album.entity.AlbumEntity
import com.pedroid.data.local.db.album.entity.AlbumRemoteKeys
import com.pedroid.data.remote.api.artists.ArtistsApi
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class SpotifyAlbumsRemoteMediator(
    private val api: ArtistsApi,
    private val db: AppRoomDataBase,
    private val artistId: String
) : RemoteMediator<Int, AlbumEntity>() {

    private val albumsDao = db.albumsDao()
    private val remoteKeysDao = db.albumsRemoteKeysDao()

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, AlbumEntity>
    ): MediatorResult {
        val offset = when (loadType) {
            LoadType.REFRESH -> 0
            LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
            LoadType.APPEND -> {
                val lastItem = state.lastItemOrNull() ?: return MediatorResult.Success(true)
                val remoteKey = remoteKeysDao.remoteKeysById(lastItem.id)
                    ?: return MediatorResult.Success(true)
                remoteKey.nextKey ?: return MediatorResult.Success(true)
            }
        }

        try {
            val response = api.getArtistAlbumsById(
                limit = state.config.pageSize,
                offset = offset,
                idArtist = artistId
            )
            val albums = response.items.map { dto ->
                AlbumEntity(
                    id = dto.id,
                    artistId = artistId,
                    name = dto.name,
                    imageUrl = dto.images?.firstOrNull()?.url,
                    releaseDate = dto.releaseDate
                )
            }

            val endReached = albums.isEmpty()

            db.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    albumsDao.clearByArtistId(artistId)
                    remoteKeysDao.clearRemoteKeysByArtistId(artistId)
                }

                albumsDao.upsertAlbums(albums)

                val keys = albums.map { artist ->
                    AlbumRemoteKeys(
                        albumId = artist.id,
                        artistId = artistId,
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
