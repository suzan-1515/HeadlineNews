package com.cognota.feed.commons.domain

import android.net.Uri
import android.os.Parcelable
import android.util.Patterns
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CategoryDTO(
    val code: String,
    val enable: String,
    val icon: String,
    val id: Int,
    val name: String,
    val nameNp: String,
    val priority: Int
) : Parcelable {
    fun icon(): Uri? {
        return if (Patterns.WEB_URL.matcher(icon).matches())
            Uri.parse(icon)
        else null
    }
}