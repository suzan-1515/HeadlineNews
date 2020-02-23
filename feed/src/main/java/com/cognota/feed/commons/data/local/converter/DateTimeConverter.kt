package com.cognota.feed.commons.data.local.converter

import androidx.room.TypeConverter
import com.cognota.core.util.DateTimeUtil
import org.joda.time.LocalDateTime

object DateTimeConverter {


    @TypeConverter
    @JvmStatic
    fun fromOffsetDateTime(date: LocalDateTime?): String? {
        return date?.let {
            DateTimeUtil.fromLocalDateTime(it, DateTimeUtil.DEFAULT_DATE_TIME_FORMAT)
        }
    }

    @TypeConverter
    @JvmStatic
    fun toOffsetDateTime(value: String?): LocalDateTime? {
        return value?.let {
            return DateTimeUtil.toLocalDateTime(it, DateTimeUtil.DEFAULT_DATE_TIME_FORMAT)
        }
    }
}