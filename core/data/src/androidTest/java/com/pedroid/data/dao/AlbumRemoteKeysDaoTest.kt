package com.pedroid.data.dao

import com.google.common.truth.Truth.assertThat
import com.pedroid.data.database.AppRoomDataBaseTest
import com.pedroid.data.local.db.album.entity.AlbumRemoteKeys
import kotlinx.coroutines.test.runTest
import org.junit.Test

internal class AlbumRemoteKeysDaoTest : AppRoomDataBaseTest() {

    @Test
    fun insertAll_and_fetchById_returnsCorrectRemoteKeys() = runTest {
        val keys = listOf(
            testRemoteKey("a1", "artist1", prev = 5, next = 10),
            testRemoteKey("a2", "artist1", prev = 6, next = 12),
        )
        albumsRemoteKeysDao.insertAll(keys)

        val key = albumsRemoteKeysDao.remoteKeysById("a2")

        assertThat(key).isNotNull()
        assertThat(key?.albumId).isEqualTo("a2")
        assertThat(key?.prevKey).isEqualTo(6)
        assertThat(key?.nextKey).isEqualTo(12)
        assertThat(key?.artistId).isEqualTo("artist1")
    }

    @Test
    fun clearRemoteKeys_removesAllEntries() = runTest {
        albumsRemoteKeysDao.insertAll(
            listOf(
                testRemoteKey("a1", "artist1", 1, 2),
                testRemoteKey("a2", "artist2", 3, 4)
            )
        )

        albumsRemoteKeysDao.clearRemoteKeys()

        val result1 = albumsRemoteKeysDao.remoteKeysById("a1")
        val result2 = albumsRemoteKeysDao.remoteKeysById("a2")

        assertThat(result1).isNull()
        assertThat(result2).isNull()
    }

    @Test
    fun clearRemoteKeysByArtistId_removesOnlyThatArtistEntries() = runTest {
        albumsRemoteKeysDao.insertAll(
            listOf(
                testRemoteKey("a1", "artist1", 1, 2),
                testRemoteKey("a2", "artist1", 3, 4),
                testRemoteKey("a3", "artist2", 5, 6),
            )
        )

        albumsRemoteKeysDao.clearRemoteKeysByArtistId("artist1")

        val remaining = albumsRemoteKeysDao.remoteKeysById("a3")
        val removed1 = albumsRemoteKeysDao.remoteKeysById("a1")
        val removed2 = albumsRemoteKeysDao.remoteKeysById("a2")

        assertThat(remaining).isNotNull()
        assertThat(removed1).isNull()
        assertThat(removed2).isNull()
    }

    private fun testRemoteKey(
        albumId: String,
        artistId: String,
        prev: Int?,
        next: Int?
    ) = AlbumRemoteKeys(
        albumId = albumId,
        artistId = artistId,
        prevKey = prev,
        nextKey = next
    )
}
