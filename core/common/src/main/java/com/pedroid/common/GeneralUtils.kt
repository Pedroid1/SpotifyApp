package com.pedroid.common

object GeneralUtils {

    fun getInitials(name: String): String {
        val words = name.trim().split("\\s+".toRegex())
        return when {
            words.size >= 2 -> "${words[0].first()}${words[1].first()}".uppercase()
            words.size == 1 && words[0].isNotEmpty() -> "${words[0].first()}".uppercase()
            else -> ""
        }
    }
}
