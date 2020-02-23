package com.cognota.feed.commons.domain

import android.net.Uri
import android.os.Parcelable
import android.text.format.DateUtils
import android.util.Patterns
import com.cognota.core.util.DateTimeUtil
import kotlinx.android.parcel.Parcelize
import org.joda.time.LocalDateTime

@Parcelize
data class FeedDTO(
    val description: String?,
    val id: String,
    val image: String?,
    val link: String?,
    val publishedDate: LocalDateTime?,
    val title: String,
    val type: FeedType,
    var source: SourceDTO,
    var category: CategoryDTO,
    var page: Int,
    val parentId: String? = null
) : Parcelable {

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

    fun publishedDateRelative(): String {
        return publishedDate?.let { date ->
            DateUtils.getRelativeTimeSpanString(
                date.toDateTime().millis
            ).toString()
        } ?: run {
            return "N/A"
        }
    }

    fun publishedDate(): String {
        return publishedDate?.let { date ->
            DateTimeUtil.fromLocalDateTime(date, DateTimeUtil.DEFAULT_DATE_FORMAT)
        } ?: run {
            return "N/A"
        }
    }

}