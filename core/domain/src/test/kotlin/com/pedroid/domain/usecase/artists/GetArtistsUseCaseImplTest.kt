package com.pedroid.domain.usecase.artists

import androidx.paging.PagingData
import com.google.common.truth.Truth.assertThat
import com.pedroid.domain.repository.ArtistsRepository
import com.pedroid.domain.usecase.artist.GetArtistsUseCase
import com.pedroid.domain.usecase.artist.GetArtistsUseCaseImpl
import com.pedroid.domain.usecase.utils.PagingUtils
import com.pedroid.model.Artist
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
class GetArtistsUseCaseImplTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var repository: ArtistsRepository
    private lateinit var useCase: GetArtistsUseCase

    @Before
    fun setUp() {
        repository = mockk()
        useCase = GetArtistsUseCaseImpl(repository)
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `execute should return expected artists`() = runTest {
        val expectedArtists = listOf(
            Artist(id = "1", name = "Artist 1", imageUrl = null),
            Artist(id = "2", name = "Artist 2", imageUrl = null)
        )
        val pagingData = PagingData.from(expectedArtists)

        coEvery { repository.getArtists() } returns flowOf(pagingData)

        val result = useCase.execute()
        val snapshot = PagingUtils.collectPagingData(result)

        assertThat(snapshot).hasSize(2)
        assertThat(snapshot[0].name).isEqualTo("Artist 1")
        assertThat(snapshot[1].name).isEqualTo("Artist 2")
    }
}
