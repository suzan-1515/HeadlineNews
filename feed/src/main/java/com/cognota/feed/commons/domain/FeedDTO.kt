package com.cognota.feed.commons.domain

import com.cognota.feed.commons.data.local.FeedType
import java.io.Serializable

data class FeedDTO(
    val category: String?,
    val description: String?,
    val id: String,
    val image: String?,
    val link: String?,
    val publishedDate: String?,
    val source: String?,
    val title: String,
    val updatedDate: String?,
    val uuid: String?,
    val type: FeedType,
    val relatedFeed: List<FeedDTO>?
) : Serializable {

    fun getFormattedDescription(): String? {
        return description?.let {
            return if (it.length <= 70)
                it
            else
                it.substring(0, 67) + "..."
        }

    }

}