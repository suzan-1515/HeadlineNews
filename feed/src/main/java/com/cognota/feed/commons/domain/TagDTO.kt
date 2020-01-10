package com.cognota.feed.commons.domain

import android.net.Uri
import android.util.Patterns
import java.io.Serializable

data class TagDTO(
    val title: String,
    val icon: String? = null
) : Serializable {

    fun icon(): Uri? {
        return icon?.let {
            if (Patterns.WEB_URL.matcher(it).matches())
                Uri.parse(icon)
            else null
        }
    }
}