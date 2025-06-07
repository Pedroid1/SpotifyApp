package com.pedroid.data.local.encriptedstorage

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SecureStorageImplInstrumentedTest {

    private lateinit var secureStorage: SecureStorage

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        secureStorage = SecureStorageImpl(context)
        secureStorage.clear()
    }

    @Test
    fun saveAndRetrieveString() {
        secureStorage.saveString("key", "value")
        val result = secureStorage.getString("key")
        assertThat(result).isEqualTo("value")
    }

    @Test
    fun saveAndRetrieveLong() {
        secureStorage.saveLong("long_key", 123L)
        val result = secureStorage.getLong("long_key")
        assertThat(result).isEqualTo(123L)
    }

    @Test
    fun clear_shouldRemoveAll() {
        secureStorage.saveString("token", "abc123")
        secureStorage.clear()
        val result = secureStorage.getString("token")
        assertThat(result).isNull()
    }
}
