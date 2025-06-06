package com.pedroid.data.dao

import com.google.common.truth.Truth.assertThat
import com.pedroid.data.database.AppRoomDataBaseTest
import com.pedroid.data.local.db.playlists.entity.PlaylistRemoteKeys
import kotlinx.coroutines.test.runTest
import org.junit.Test

internal class PlaylistRemoteKeysDaoTest : AppRoomDataBaseTest() {

    @Test
    fun insertAll_and_fetchById_returnsCorrectRemoteKeys() = runTest {
        val keys = listOf(
            testRemoteKey("1", prev = 10, next = 20),
            testRemoteKey("2", prev = 5, next = 15)
        )
        playlistRemoteKeysDao.insertAll(keys)

        val key = playlistRemoteKeysDao.remoteKeysById("2")

        assertThat(key).isNotNull()
        assertThat(key?.playlistId).isEqualTo("2")
        assertThat(key?.prevKey).isEqualTo(5)
        assertThat(key?.nextKey).isEqualTo(15)
    }

    @Test
    fun clearRemoteKeys_removesAllEntries() = runTest {
        playlistRemoteKeysDao.insertAll(
            listOf(
                testRemoteKey("1", 1, 2),
                testRemoteKey("2", 2, 3)
            )
        )

        playlistRemoteKeysDao.clearRemoteKeys()

        val key1 = playlistRemoteKeysDao.remoteKeysById("1")
        val key2 = playlistRemoteKeysDao.remoteKeysById("2")

        assertThat(key1).isNull()
        assertThat(key2).isNull()
    }

    private fun testRemoteKey(
        id: String,
        prev: Int?,
        next: Int?
    ) = PlaylistRemoteKeys(
        playlistId = id,
        prevKey = prev,
        nextKey = next
    )
}
