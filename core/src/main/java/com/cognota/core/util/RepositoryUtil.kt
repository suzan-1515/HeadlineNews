package com.cognota.core.util

import android.content.SharedPreferences

object RepositoryUtil {

    fun getSecondsSinceEpoch() = System.currentTimeMillis()

    /**
     *
     * @param cacheKey String - A unique string to represent the cache, ex: GetArtist
     * @param keyDescriptor String - A string to give a secondary description ex: ACDC
     * @param cacheLengthSeconds Long - How long is the cache considered fresh (use TimeUnit.[MINUTES/HOURS/DAYS].toSeconds(x))
     * @return Boolean
     */
    @Synchronized
    fun shouldFetch(
        sharedPreferences: SharedPreferences,
        cacheKey: String,
        keyDescriptor: String? = "",
        cacheLengthSeconds: Long
    ): Boolean {
        val lastCacheCurrentSeconds =
            sharedPreferences.getLong(cacheKey + "_" + keyDescriptor, -1L)
        if (lastCacheCurrentSeconds == -1L) {
            resetCache(sharedPreferences, cacheKey, keyDescriptor)
            return true
        }
        if (getSecondsSinceEpoch().minus(lastCacheCurrentSeconds) > cacheLengthSeconds) {
            resetCache(sharedPreferences, cacheKey, keyDescriptor)
            return true
        }
        return false
    }

    @Synchronized
    fun resetCache(
        sharedPreferences: SharedPreferences,
        cacheKey: String,
        keyDescriptor: String? = ""
    ) {
        sharedPreferences
            .edit()
            .putLong(
                cacheKey + "_" + keyDescriptor,
                getSecondsSinceEpoch()
            )
            .apply()
    }
}