package com.pedroid.domain.usecase.playlist

import androidx.paging.PagingData
import com.google.common.truth.Truth.assertThat
import com.pedroid.domain.repository.PlaylistRepository
import com.pedroid.domain.usecase.utils.PagingUtils
import com.pedroid.model.Playlist
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GetPlaylistsUseCaseImplTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var repository: PlaylistRepository
    private lateinit var useCase: GetPlaylistsUseCase

    @Before
    fun setUp() {
        repository = mockk()
        useCase = GetPlaylistsUseCaseImpl(repository)
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `execute should return expected playlists`() = runTest {
        val expectedPlaylists = listOf(
            Playlist(id = "1", name = "Playlist 1", imageUrl = null, description = "desc 1"),
            Playlist(id = "2", name = "Playlist 2", imageUrl = null, description = "desc 2")
        )
        val pagingData = PagingData.from(expectedPlaylists)

        coEvery { repository.getPlaylists() } returns flowOf(pagingData)

        val result = useCase.execute()
        val snapshot = PagingUtils.collectPagingData(result)

        assertThat(snapshot).hasSize(2)
        assertThat(snapshot[0].name).isEqualTo("Playlist 1")
        assertThat(snapshot[1].name).isEqualTo("Playlist 2")
    }
}
