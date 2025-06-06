package com.pedroid.data.dao.playlists

import androidx.paging.PagingSource
import com.google.common.truth.Truth.assertThat
import com.pedroid.data.database.AppRoomDataBaseTest
import com.pedroid.data.local.db.playlists.entity.PlaylistEntity
import kotlinx.coroutines.test.runTest
import org.junit.Test

internal class PlaylistDaoTest : AppRoomDataBaseTest() {

    @Test
    fun upsertPlaylists_insertsNewEntries() = runTest {
        val playlists = listOf(
            testPlaylistEntity("1", "Playlist A"),
            testPlaylistEntity("2", "Playlist B"),
            testPlaylistEntity("3", "Playlist C"),
        )
        playlistDao.upsertPlaylists(playlists)

        val result = playlistDao.pagingSource().load(
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
    fun upsertPlaylists_updatesExistingEntries() = runTest {
        playlistDao.upsertPlaylists(listOf(testPlaylistEntity("1", "Old Playlist")))
        playlistDao.upsertPlaylists(listOf(testPlaylistEntity("1", "Updated Playlist")))

        val result = playlistDao.pagingSource().load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 10,
                placeholdersEnabled = false
            )
        ) as PagingSource.LoadResult.Page

        assertThat(result.data).hasSize(1)
        assertThat(result.data.first().name).isEqualTo("Updated Playlist")
    }

    @Test
    fun clearAll_removesAllPlaylists() = runTest {
        playlistDao.upsertPlaylists(
            listOf(
                testPlaylistEntity("1", "Playlist A"),
                testPlaylistEntity("2", "Playlist B")
            )
        )

        playlistDao.clearAll()

        val result = playlistDao.pagingSource().load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 10,
                placeholdersEnabled = false
            )
        ) as PagingSource.LoadResult.Page

        assertThat(result.data).isEmpty()
    }

    private fun testPlaylistEntity(
        id: String,
        name: String
    ) = PlaylistEntity(
        id = id,
        name = name,
        description = "",
        imageUrl = ""
    )
}
