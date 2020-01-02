package com.cognota.feed.commons.domain

import android.net.Uri
import android.text.format.DateUtils
import android.util.Patterns
import com.cognota.core.util.DateTimeUtil
import com.cognota.feed.commons.data.local.FeedType
import java.io.Serializable
import java.util.*

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
    val relatedFeed: List<FeedDTO>?,
    var sourceDTO: SourceDTO? = null,
    var categoryDTO: CategoryDTO? = null
) : Serializable {

    fun thumbnail(): Uri? {
        return image?.let {
            return if (Patterns.WEB_URL.matcher(it).matches())
                Uri.parse(it)
            else null
        }
    }

    fun link(): Uri? {
        return link?.let {
            return if (Patterns.WEB_URL.matcher(it).matches())
                Uri.parse(it)
            else null
        }
    }

    fun publishedDate(): String {
        return publishedDate?.let { date ->
            var dateTime = "N/A"
            DateTimeUtil.parse(date)?.let {
                dateTime = DateUtils.getRelativeTimeSpanString(
                    it.time,
                    Date().time,
                    DateUtils.MINUTE_IN_MILLIS
                ).toString()
            }
            return dateTime
        } ?: run {
            return "N/A"
        }
    }

}