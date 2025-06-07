package com.pedroid.data.remote.paging

import androidx.paging.*
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.pedroid.data.local.db.AppRoomDataBase
import com.pedroid.data.local.db.album.entity.AlbumEntity
import com.pedroid.data.remote.api.artists.dto.AlbumDto
import com.pedroid.data.remote.api.dto.ImageDto
import com.pedroid.testing.repository.FakeArtistsApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalPagingApi::class)
class SpotifyAlbumsRemoteMediatorTest {

    private lateinit var db: AppRoomDataBase
    private lateinit var fakeApi: FakeArtistsApi
    private lateinit var remoteMediator: SpotifyAlbumsRemoteMediator

    private val artistId = "artist_1"

    @Before
    fun setup() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppRoomDataBase::class.java
        ).build()

        fakeApi = FakeArtistsApi()

        remoteMediator = SpotifyAlbumsRemoteMediator(
            api = fakeApi,
            db = db,
            artistId = artistId
        )
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun refreshLoadReturnsSuccessAndInsertsData() = runTest {
        val albums = (1..10).map {
            AlbumDto(
                id = "album_$it",
                name = "Album $it",
                images = listOf(ImageDto("https://img$it.com")),
                releaseDate = "2025-01-0$it"
            )
        }

        fakeApi.setAlbumsForArtist(artistId, *albums.toTypedArray())

        val pagingState = PagingState<Int, AlbumEntity>(
            pages = emptyList(),
            anchorPosition = null,
            config = PagingConfig(pageSize = 5),
            leadingPlaceholderCount = 0
        )

        val result = remoteMediator.load(LoadType.REFRESH, pagingState)

        assertTrue(result is RemoteMediator.MediatorResult.Success)
        assertTrue(!(result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)

        val itemsInDb = db.albumsDao().pagingSource(artistId).load(
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
        val pagingState = PagingState<Int, AlbumEntity>(
            pages = listOf(),
            anchorPosition = null,
            config = PagingConfig(pageSize = 5),
            leadingPlaceholderCount = 0
        )

        val result = remoteMediator.load(LoadType.APPEND, pagingState)

        assertTrue(result is RemoteMediator.MediatorResult.Success)
        assertTrue((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)
    }
}
