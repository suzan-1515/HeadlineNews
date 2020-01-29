package com.cognota.feed.commons.domain

import android.net.Uri
import android.os.Parcelable
import android.util.Patterns
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TagDTO(
    val title: String,
    val icon: String? = null
) : Parcelable {

    fun icon(): Uri? {
        return icon?.let {
            if (Patterns.WEB_URL.matcher(it).matches())
                Uri.parse(icon)
            else null
        }
    }
}