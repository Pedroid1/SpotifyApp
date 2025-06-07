package com.pedroid.data.local.db.dao.albums

import androidx.paging.PagingSource
import com.google.common.truth.Truth.assertThat
import com.pedroid.data.local.db.database.AppRoomDataBaseTest
import com.pedroid.data.local.db.album.entity.AlbumEntity
import kotlinx.coroutines.test.runTest
import org.junit.Test

internal class AlbumDaoTest : AppRoomDataBaseTest() {

    @Test
    fun upsertAlbums_insertsAndQueriesByArtistId() = runTest {
        val albums = listOf(
            testAlbumEntity("a1", "1", "Album A"),
            testAlbumEntity("a2", "1", "Album B"),
            testAlbumEntity("a3", "2", "Album C"),
        )
        albumsDao.upsertAlbums(albums)

        val result = albumsDao.pagingSource("1").load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 10,
                placeholdersEnabled = false
            )
        ) as PagingSource.LoadResult.Page

        assertThat(result.data).hasSize(2)
        assertThat(result.data.map { it.id }).containsExactly("a1", "a2").inOrder()
    }

    @Test
    fun clearByArtistId_removesOnlyThatArtistAlbums() = runTest {
        albumsDao.upsertAlbums(
            listOf(
                testAlbumEntity("a1", "1", "Album A"),
                testAlbumEntity("a2", "1", "Album B"),
                testAlbumEntity("a3", "2", "Album C")
            )
        )

        albumsDao.clearByArtistId("1")

        val result = albumsDao.pagingSource("1").load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 10,
                placeholdersEnabled = false
            )
        ) as PagingSource.LoadResult.Page

        assertThat(result.data).isEmpty()

        val remaining = albumsDao.pagingSource("2").load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 10,
                placeholdersEnabled = false
            )
        ) as PagingSource.LoadResult.Page

        assertThat(remaining.data.map { it.id }).containsExactly("a3")
    }

    @Test
    fun clearAll_removesAllAlbums() = runTest {
        albumsDao.upsertAlbums(
            listOf(
                testAlbumEntity("a1", "1", "Album A"),
                testAlbumEntity("a2", "2", "Album B")
            )
        )

        albumsDao.clearAll()

        val result1 = albumsDao.pagingSource("1").load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 10,
                placeholdersEnabled = false
            )
        ) as PagingSource.LoadResult.Page

        val result2 = albumsDao.pagingSource("2").load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 10,
                placeholdersEnabled = false
            )
        ) as PagingSource.LoadResult.Page

        assertThat(result1.data).isEmpty()
        assertThat(result2.data).isEmpty()
    }

    private fun testAlbumEntity(
        id: String,
        artistId: String,
        name: String
    ) = AlbumEntity(
        id = id,
        artistId = artistId,
        name = name,
        imageUrl = "",
        releaseDate = ""
    )
}
