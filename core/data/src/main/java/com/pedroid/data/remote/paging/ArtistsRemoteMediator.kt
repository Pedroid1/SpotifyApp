package com.pedroid.data.remote.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.pedroid.data.local.AppRoomDataBase
import com.pedroid.data.local.artists.entity.ArtistEntity
import com.pedroid.data.local.artists.entity.ArtistRemoteKeys
import com.pedroid.data.remote.artists.ArtistsApi
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class SpotifyArtistRemoteMediator(
    private val api: ArtistsApi,
    private val db: AppRoomDataBase
) : RemoteMediator<Int, ArtistEntity>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ArtistEntity>
    ): MediatorResult {

        val artistDao = db.artistDao()
        val remoteKeysDao = db.artistRemoteKeys()

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
            val response = api.getArtists(limit = state.config.pageSize, offset = offset)
            val artists = response.items.map { dto ->
                ArtistEntity(
                    id = dto.id,
                    name = dto.name,
                    imageUrl = dto.images.firstOrNull()?.url
                )
            }

            val endReached = artists.isEmpty()

            db.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    artistDao.clearAll()
                    remoteKeysDao.clearRemoteKeys()
                }

                artistDao.upsertArtists(artists)

                val keys = artists.map { artist ->
                    ArtistRemoteKeys(
                        artistId = artist.id,
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
        } catch (e: Exception) {
            return MediatorResult.Error(e)
        }
    }
}
