package com.cognota.feed.commons.domain

import android.net.Uri
import android.os.Parcelable
import android.text.format.DateUtils
import android.util.Patterns
import com.cognota.core.util.DateTimeUtil
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class FeedDTO(
    val description: String?,
    val id: String,
    val image: String?,
    val link: String?,
    val publishedDate: String?,
    val title: String,
    val updatedDate: String?,
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

    fun publishedDate(): String {
        return publishedDate?.let { date ->
            var dateTime = "N/A"
            return try {
                DateTimeUtil.parse(date)?.let {
                    dateTime = DateUtils.getRelativeTimeSpanString(
                        it.time,
                        Date().time,
                        DateUtils.MINUTE_IN_MILLIS
                    ).toString()
                }
                dateTime
            } catch (e: Exception) {
                date
            }
        } ?: run {
            return "N/A"
        }
    }

    fun publishedDateRaw(): String {
        return publishedDate?.let { date ->
            var dateTime = "N/A"
            return try {
                DateTimeUtil.format(DateTimeUtil.parse(date))?.let {
                    dateTime = it
                }
                dateTime
            } catch (e: Exception) {
                date
            }
        } ?: run {
            return "N/A"
        }
    }

}