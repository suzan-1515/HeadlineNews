package com.cognota.feed.commons.domain

import android.net.Uri
import android.text.format.DateUtils
import android.util.Patterns
import com.cognota.core.util.DateTimeUtil
import java.io.Serializable
import java.util.*

data class BookmarkDTO(
    val categoryCode: String,
    val categoryIcon: String?,
    val categoryName: String,
    val description: String?,
    val id: String,
    val image: String?,
    val link: String?,
    val publishedDate: String?,
    val sourceCode: String,
    val sourceFavicon: String?,
    val sourceIcon: String?,
    val sourceName: String,
    val title: String,
    val updateDate: String?

) : Serializable {
    fun sourceIcon(): Uri? {
        return sourceIcon?.let {
            return if (Patterns.WEB_URL.matcher(it).matches())
                Uri.parse(it)
            else null
        }
    }

    fun categoryIcon(): Uri? {
        return categoryIcon?.let {
            return if (Patterns.WEB_URL.matcher(it).matches())
                Uri.parse(it)
            else null
        }
    }

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

    fun publishedDateRaw(): String {
        return publishedDate?.let { date ->
            var dateTime = "N/A"
            DateTimeUtil.format(DateTimeUtil.parse(date))?.let {
                dateTime = it
            }
            return dateTime
        } ?: run {
            return "N/A"
        }
    }

    fun toFeedDto(): FeedDTO {
        return FeedDTO(
            id = id,
            description = description,
            image = image,
            link = link,
            publishedDate = publishedDate,
            title = title,
            updatedDate = updateDate,
            type = FeedType.LATEST,
            source = SourceDTO(
                id = -1,
                code = sourceCode,
                favicon = sourceFavicon,
                icon = sourceIcon,
                name = sourceName,
                priority = -1
            ),
            category = CategoryDTO(
                id = -1,
                code = categoryCode,
                icon = categoryIcon?.let { it } ?: "",
                name = categoryName,
                priority = -1,
                enable = "true",
                nameNp = categoryName
            ),
            page = 1
        )
    }

}