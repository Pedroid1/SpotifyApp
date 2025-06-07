package com.pedroid.data.local.db.dao.artists

import androidx.paging.PagingSource
import com.google.common.truth.Truth.assertThat
import com.pedroid.data.local.db.database.AppRoomDataBaseTest
import com.pedroid.data.local.db.artists.entity.ArtistEntity
import kotlinx.coroutines.test.runTest
import org.junit.Test

internal class ArtistsDaoTest : AppRoomDataBaseTest() {

    @Test
    fun upsertArtists_insertsNewEntries() = runTest {
        val artists = listOf(
            testArtistEntity("1", "Artist 1"),
            testArtistEntity("2", "Artist 2"),
            testArtistEntity("3", "Artist 3"),
        )
        artistsDao.upsertArtists(artists)

        val result = artistsDao.pagingSource().load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 10,
                placeholdersEnabled = false
            )
        ) as PagingSource.LoadResult.Page

        assertThat(result.data).hasSize(3)
        assertThat(result.data.map { it.id }).containsExactly("1", "2", "3").inOrder()
    }

    @Test
    fun upsertArtists_updatesExistingEntries() = runTest {
        artistsDao.upsertArtists(listOf(testArtistEntity("1", "Old Name")))
        artistsDao.upsertArtists(listOf(testArtistEntity("1", "Updated Name")))

        val result = artistsDao.pagingSource().load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 10,
                placeholdersEnabled = false
            )
        ) as PagingSource.LoadResult.Page

        assertThat(result.data).hasSize(1)
        assertThat(result.data.first().name).isEqualTo("Updated Name")
    }

    @Test
    fun clearAll_removesAllEntries() = runTest {
        artistsDao.upsertArtists(
            listOf(
                testArtistEntity("1", "Artist 1"),
                testArtistEntity("2", "Artist 2")
            )
        )

        artistsDao.clearAll()

        val result = artistsDao.pagingSource().load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 10,
                placeholdersEnabled = false
            )
        ) as PagingSource.LoadResult.Page

        assertThat(result.data).isEmpty()
    }

    private fun testArtistEntity(
        id: String,
        name: String
    ) = ArtistEntity(
        id = id,
        name = name,
        imageUrl = "",
    )
}
