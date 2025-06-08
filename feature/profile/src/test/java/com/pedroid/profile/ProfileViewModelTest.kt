package com.pedroid.profile

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.pedroid.common.core.DataResource
import com.pedroid.common.core.UiText
import com.pedroid.domain.session.SessionManager
import com.pedroid.domain.usecase.user.GetUserProfileUseCase
import com.pedroid.eventbus.AppEvent
import com.pedroid.eventbus.EventBusController
import com.pedroid.model.UserProfile
import getOrAwaitValue
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.*
import org.junit.*
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class ProfileViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    private val mockGetUserProfileUseCase: GetUserProfileUseCase = mockk(relaxed = true)
    private val mockSessionManager: SessionManager = mockk(relaxed = true)
    private val mockEventBusController: EventBusController = mockk(relaxed = true)

    private lateinit var viewModel: ProfileViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `state should emit loading and userProfile on success`() = runTest {
        val fakeProfile = UserProfile(id = "10", displayName = "Maria", imageUrl = null)
        coEvery { mockGetUserProfileUseCase.getUserProfile() } returns DataResource.Success(fakeProfile)

        viewModel = ProfileViewModel(
            getUserProfileUseCase = mockGetUserProfileUseCase,
            sessionManager = mockSessionManager,
            eventBusController = mockEventBusController
        )

        val emissions = mutableListOf<ProfileUiState>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect { emissions.add(it) }
        }

        advanceUntilIdle()

        assertTrue(emissions.any { it.isLoading })
        assertEquals(fakeProfile, emissions.last().userProfile)
        assertFalse(emissions.last().isLoading)
    }

    @Test
    fun `state should emit error event when fetch fails`() = runTest {
        coEvery { mockGetUserProfileUseCase.getUserProfile() } returns DataResource.Error(RuntimeException())

        viewModel = ProfileViewModel(
            getUserProfileUseCase = mockGetUserProfileUseCase,
            sessionManager = mockSessionManager,
            eventBusController = mockEventBusController
        )

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect()
        }

        advanceUntilIdle()

        val value = viewModel.errorEvent.getOrAwaitValue()
        Assert.assertTrue(value.getContentIfNotHandled() is UiText.StringResource)
    }

    @Test
    fun `logout should clear session and publish logout event`() = runTest {
        viewModel = ProfileViewModel(
            getUserProfileUseCase = mockGetUserProfileUseCase,
            sessionManager = mockSessionManager,
            eventBusController = mockEventBusController
        )

        viewModel.logout()

        advanceUntilIdle()

        coVerify { mockSessionManager.clearSession() }
        coVerify { mockEventBusController.publishEvent(AppEvent.LOGOUT) }
    }
}
