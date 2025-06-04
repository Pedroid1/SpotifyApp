package com.pedroid.data.local.encriptedstorage

interface SecureStorage {
    fun saveString(key: String, value: String)
    fun getString(key: String): String?
    fun saveLong(key: String, value: Long)
    fun getLong(key: String): Long?
    fun clear()
}
