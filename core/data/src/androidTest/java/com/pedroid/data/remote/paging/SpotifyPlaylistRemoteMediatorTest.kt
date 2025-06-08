package com.pedroid.data.remote.paging

import androidx.paging.*
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.pedroid.data.local.db.AppRoomDataBase
import com.pedroid.data.local.db.playlists.entity.PlaylistEntity
import com.pedroid.data.remote.api.playlists.dto.PlaylistDto
import com.pedroid.testing.repository.FakePlaylistsApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalPagingApi::class)
class SpotifyPlaylistRemoteMediatorTest {

    private lateinit var db: AppRoomDataBase
    private lateinit var fakeApi: FakePlaylistsApi
    private lateinit var remoteMediator: SpotifyPlaylistRemoteMediator

    @Before
    fun setup() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppRoomDataBase::class.java
        ).build()

        fakeApi = FakePlaylistsApi()

        remoteMediator = SpotifyPlaylistRemoteMediator(
            api = fakeApi,
            db = db
        )
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun refreshLoadReturnsSuccessAndInsertsData() = runTest {
        val playlists = (1..10).map {
            PlaylistDto(
                id = "playlist_$it",
                name = "Playlist $it",
                description = "Desc $it",
                images = listOf()
            )
        }
        fakeApi.setPlaylists(playlists)

        val pagingState = PagingState<Int, PlaylistEntity>(
            pages = emptyList(),
            anchorPosition = null,
            config = PagingConfig(pageSize = 5),
            leadingPlaceholderCount = 0
        )

        val result = remoteMediator.load(LoadType.REFRESH, pagingState)

        assertTrue(result is RemoteMediator.MediatorResult.Success)
        assertTrue(!(result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)

        val itemsInDb = db.playlistDao().pagingSource().load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 5,
                placeholdersEnabled = false
            )
        )

        assertTrue(itemsInDb is PagingSource.LoadResult.Page)
        val data = (itemsInDb as PagingSource.LoadResult.Page).data
        assertTrue(data.isNotEmpty())
    }

    @Test
    fun appendLoadReturnsSuccessWhenNoMoreKeys() = runTest {
        val pagingState = PagingState<Int, PlaylistEntity>(
            pages = listOf(),
            anchorPosition = null,
            config = PagingConfig(pageSize = 5),
            leadingPlaceholderCount = 0
        )

        val result = remoteMediator.load(LoadType.APPEND, pagingState)

        assertTrue(result is RemoteMediator.MediatorResult.Success)
        assertTrue((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)
    }

    @Test
    fun loadReturnsErrorOnNetworkFailure() = runTest {
        fakeApi.throwOnRequest(true)

        val pagingState = PagingState<Int, PlaylistEntity>(
            pages = emptyList(),
            anchorPosition = null,
            config = PagingConfig(pageSize = 5),
            leadingPlaceholderCount = 0
        )

        val result = remoteMediator.load(LoadType.REFRESH, pagingState)

        assertTrue(result is RemoteMediator.MediatorResult.Error)
    }
}
