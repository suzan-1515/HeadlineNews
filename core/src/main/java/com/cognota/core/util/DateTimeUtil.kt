package com.cognota.core.util

import org.joda.time.LocalDateTime
import org.joda.time.format.DateTimeFormat
import java.text.SimpleDateFormat
import java.util.*


object DateTimeUtil {
    const val DEFAULT_SERVER_DATE_TIME_FORMAT = "yyyy-MM-dd hh:mm:ss Z"
    const val DEFAULT_DATE_TIME_FORMAT = "yyyy-MM-dd hh:mm:ss"
    const val DEFAULT_DATE_FORMAT = "d MMM, EEEE, yyyy"

    private fun toCalendar(date: String?, format: String): Calendar? {
        return date?.let {
            SimpleDateFormat(format, Locale.getDefault()).parse(it)?.let { d ->
                val instance = Calendar.getInstance()
                instance.timeInMillis = d.time
                return instance
            }
        }
    }


    fun fromLocalDateTime(date: LocalDateTime, format: String): String? {
        return date.toString(DateTimeFormat.forPattern(format))
    }

    fun toLocalDateTime(date: String, format: String): LocalDateTime? {
        return toCalendar(date, format)?.let {
            LocalDateTime.fromCalendarFields(it)
        }
    }

}