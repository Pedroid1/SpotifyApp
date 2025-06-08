package com.pedroid.playlist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.pedroid.common.core.DataResource
import com.pedroid.common.core.UiText
import com.pedroid.domain.usecase.playlist.CreatePlaylistUseCase
import com.pedroid.domain.usecase.playlist.GetPlaylistsUseCase
import com.pedroid.domain.usecase.user.GetUserProfileUseCase
import com.pedroid.model.UserProfile
import getOrAwaitValue
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PlaylistViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    private val mockGetUserProfileUseCase: GetUserProfileUseCase = mockk(relaxed = true)
    private val mockGetPlaylistUseCase: GetPlaylistsUseCase = mockk(relaxed = true)
    private val mockCretePlaylistUseCase: CreatePlaylistUseCase = mockk(relaxed = true)

    private lateinit var viewModel: PlaylistViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `state should emit loading true then success with userProfile and loading false`() =
        runTest {
            val fakeProfile = UserProfile(id = "1", displayName = "Zé", imageUrl = null)

            coEvery { mockGetUserProfileUseCase.getUserProfile() } returns DataResource.Success(
                fakeProfile
            )

            viewModel = PlaylistViewModel(
                getUserProfileUseCase = mockGetUserProfileUseCase,
                playlistUseCase = mockGetPlaylistUseCase,
                createPlaylistUseCase = mockCretePlaylistUseCase
            )

            val emissions = mutableListOf<PlaylistUiState>()
            backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
                viewModel.state.collect { emissions.add(it) }
            }

            advanceUntilIdle()

            assertTrue(emissions.any { it.isLoading })
            assertEquals(fakeProfile, emissions.last().userProfile)
            assertFalse(emissions.last().isLoading)
        }

    @Test
    fun `when getUserProfile returns error then errorEvent should emit UiText_StringResource`() =
        runTest {
            val exception = RuntimeException("Erro simulado")

            coEvery { mockGetUserProfileUseCase.getUserProfile() } returns DataResource.Error(
                exception
            )

            viewModel = PlaylistViewModel(
                getUserProfileUseCase = mockGetUserProfileUseCase,
                playlistUseCase = mockGetPlaylistUseCase,
                createPlaylistUseCase = mockCretePlaylistUseCase
            )

            backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
                viewModel.state.collect()
            }

            advanceUntilIdle()

            val value = viewModel.errorEvent.getOrAwaitValue()
            assert(value.getContentIfNotHandled() is UiText.StringResource)
        }

    @Test
    fun `createPlaylist should trigger refresh event on success`() = runTest {
        coEvery { mockCretePlaylistUseCase.execute("Nova Playlist") } returns DataResource.Success(
            Unit
        )

        viewModel = PlaylistViewModel(
            getUserProfileUseCase = mockGetUserProfileUseCase,
            playlistUseCase = mockGetPlaylistUseCase,
            createPlaylistUseCase = mockCretePlaylistUseCase
        )

        viewModel.createPlaylist("Nova Playlist")

        advanceUntilIdle()

        val value = viewModel.refreshTrigger.getOrAwaitValue()
        Assert.assertNotNull(value.getContentIfNotHandled())
    }
}
