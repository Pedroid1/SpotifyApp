package com.pedroid.data.dao

import com.google.common.truth.Truth.assertThat
import com.pedroid.data.database.AppRoomDataBaseTest
import com.pedroid.data.local.db.artists.entity.ArtistRemoteKeys
import kotlinx.coroutines.test.runTest
import org.junit.Test

internal class ArtistsRemoteKeysDaoTest : AppRoomDataBaseTest() {

    @Test
    fun insertAll_and_fetchById_returnsCorrectRemoteKeys() = runTest {
        val keys = listOf(
            testRemoteKey("1", 10, 20),
            testRemoteKey("2", 5, 15),
        )
        artistsRemoteKeysDao.insertAll(keys)

        val key = artistsRemoteKeysDao.remoteKeysById("2")

        assertThat(key).isNotNull()
        assertThat(key?.artistId).isEqualTo("2")
        assertThat(key?.prevKey).isEqualTo(5)
        assertThat(key?.nextKey).isEqualTo(15)
    }

    @Test
    fun clearRemoteKeys_removesAllEntries() = runTest {
        artistsRemoteKeysDao.insertAll(
            listOf(
                testRemoteKey("1", 1, 2),
                testRemoteKey("2", 2, 3)
            )
        )

        artistsRemoteKeysDao.clearRemoteKeys()

        val key1 = artistsRemoteKeysDao.remoteKeysById("1")
        val key2 = artistsRemoteKeysDao.remoteKeysById("2")

        assertThat(key1).isNull()
        assertThat(key2).isNull()
    }

    private fun testRemoteKey(
        id: String,
        prevKey: Int?,
        nextKey: Int?
    ) = ArtistRemoteKeys(
        artistId = id,
        prevKey = prevKey,
        nextKey = nextKey
    )
}
