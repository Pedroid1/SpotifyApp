package com.pedroid.data.repository.playlist

import androidx.paging.ExperimentalPagingApi
import com.google.common.truth.Truth.assertThat
import com.pedroid.common.core.DataResource
import com.pedroid.data.local.db.AppRoomDataBase
import com.pedroid.data.remote.api.playlists.PlaylistsApi
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException
import java.io.IOException

@ExperimentalPagingApi
class PlaylistRepositoryImplTest {

    private lateinit var api: PlaylistsApi
    private lateinit var db: AppRoomDataBase
    private lateinit var repository: PlaylistRepositoryImpl

    @Before
    fun setUp() {
        api = mockk()
        db = mockk()
        repository = PlaylistRepositoryImpl(
            ioDispatcher = Dispatchers.Unconfined,
            api = api,
            db = db
        )
    }

    @Test
    fun `createPlaylist returns Success when API call succeeds`() = runTest {
        coEvery {
            api.createPlaylist(any(), any())
        } just Runs

        val result = repository.createPlaylist("user123", "Chill Vibes")

        assertThat(result).isInstanceOf(DataResource.Success::class.java)
    }

    @Test
    fun `createPlaylist returns Error when HttpException is thrown`() = runTest {
        coEvery {
            api.createPlaylist(any(), any())
        } throws mockk<HttpException>()

        val result = repository.createPlaylist("user123", "Chill Vibes")

        assertThat(result).isInstanceOf(DataResource.Error::class.java)
    }

    @Test
    fun `createPlaylist returns Error when IOException is thrown`() = runTest {
        coEvery {
            api.createPlaylist(any(), any())
        } throws IOException()

        val result = repository.createPlaylist("user123", "Chill Vibes")

        assertThat(result).isInstanceOf(DataResource.Error::class.java)
    }
}
