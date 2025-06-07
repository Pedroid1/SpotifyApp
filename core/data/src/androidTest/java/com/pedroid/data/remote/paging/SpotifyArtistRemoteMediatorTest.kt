package com.pedroid.data.remote.paging

import androidx.paging.*
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.pedroid.data.local.db.AppRoomDataBase
import com.pedroid.data.local.db.artists.entity.ArtistEntity
import com.pedroid.data.remote.api.artists.dto.ArtistDto
import com.pedroid.data.remote.api.dto.ImageDto
import com.pedroid.testing.repository.FakeArtistsApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalPagingApi::class)
class SpotifyArtistRemoteMediatorTest {

    private lateinit var db: AppRoomDataBase
    private lateinit var fakeApi: FakeArtistsApi
    private lateinit var remoteMediator: SpotifyArtistRemoteMediator

    @Before
    fun setup() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppRoomDataBase::class.java
        ).build()

        fakeApi = FakeArtistsApi()

        remoteMediator = SpotifyArtistRemoteMediator(
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
        val artists = (1..10).map {
            ArtistDto(
                id = "artist_$it",
                name = "Artist $it",
                images = listOf(ImageDto("https://img$it.com"))
            )
        }
        fakeApi.setArtists(*artists.toTypedArray())

        val pagingState = PagingState<Int, ArtistEntity>(
            pages = emptyList(),
            anchorPosition = null,
            config = PagingConfig(pageSize = 5),
            leadingPlaceholderCount = 0
        )

        val result = remoteMediator.load(LoadType.REFRESH, pagingState)

        assertTrue(result is RemoteMediator.MediatorResult.Success)
        assertTrue(!(result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)

        val itemsInDb = db.artistDao().pagingSource().load(
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
        val pagingState = PagingState<Int, ArtistEntity>(
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
