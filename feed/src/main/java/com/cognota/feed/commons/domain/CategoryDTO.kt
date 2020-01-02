package com.cognota.feed.commons.domain

import android.net.Uri
import android.util.Patterns
import java.io.Serializable

data class CategoryDTO(
    val code: String,
    val enable: String,
    val icon: String,
    val id: Int,
    val name: String,
    val nameNp: String,
    val priority: Int
) : Serializable {

    fun icon(): Uri? {
        return if (Patterns.WEB_URL.matcher(icon).matches())
            Uri.parse(icon)
        else null
    }
}