package com.cognota.feed.commons.domain

import android.net.Uri
import android.util.Patterns
import java.io.Serializable

data class SourceDTO(
    val code: String,
    var favicon: String? = null,
    var icon: String? = null,
    val id: Int,
    val name: String,
    val priority: Int
) : Serializable {
    fun icon(): Uri? {
        return icon?.let {
            return if (Patterns.WEB_URL.matcher(it).matches())
                Uri.parse(it)
            else null
        }
    }

    fun favIcon(): Uri? {
        return favicon?.let {
            return if (Patterns.WEB_URL.matcher(it).matches())
                Uri.parse(it)
            else null
        }
    }
}