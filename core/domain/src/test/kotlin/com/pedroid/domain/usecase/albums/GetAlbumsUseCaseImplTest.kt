package com.pedroid.domain.usecase.albums

import androidx.paging.PagingData
import com.google.common.truth.Truth.assertThat
import com.pedroid.domain.repository.ArtistsRepository
import com.pedroid.domain.usecase.utils.PagingUtils
import com.pedroid.model.Album
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
class GetAlbumsUseCaseImplTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var repository: ArtistsRepository
    private lateinit var useCase: GetAlbumsUseCase

    @Before
    fun setUp() {
        repository = mockk()
        useCase = GetAlbumsUseCaseImpl(repository)
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `execute should return expected albums`() = runTest {
        val artistId = "artist_1"
        val expectedAlbums = listOf(
            Album(id = "1", name = "Album 1", imageUrl = null, releaseDate = "2022-01-01"),
            Album(id = "2", name = "Album 2", imageUrl = null, releaseDate = "2023-02-02")
        )
        val pagingData = PagingData.from(expectedAlbums)

        coEvery { repository.getArtistAlbumsById(artistId) } returns flowOf(pagingData)

        val result = useCase.execute(artistId)
        val snapshot = PagingUtils.collectPagingData(result)

        assertThat(snapshot).hasSize(2)
        assertThat(snapshot[0].name).isEqualTo("Album 1")
        assertThat(snapshot[1].name).isEqualTo("Album 2")
    }
}
