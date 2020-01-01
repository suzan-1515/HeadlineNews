package com.cognota.core.data.persistence.converter

import androidx.room.TypeConverter
import com.cognota.core.util.DateTimeUtil
import java.util.*

class BaseConverter {

    @TypeConverter
    fun fromOffsetDateTime(date: Date): String? {
        return DateTimeUtil.parse(date)
    }

    @TypeConverter
    fun toOffsetDateTime(value: String): Date? {
        return DateTimeUtil.parse(value)
    }
}