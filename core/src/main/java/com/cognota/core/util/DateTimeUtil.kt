package com.cognota.core.util

import java.text.SimpleDateFormat
import java.util.*


object DateTimeUtil {
    const val DEFAULT_DATE_TIME_FORMAT = "yyyy-MM-dd hh:mm:ss"
    const val DEFAULT_DATE_FORMAT = "d MMM, EEEE, yyyy"

    var serverDateFormat: SimpleDateFormat =
        SimpleDateFormat("yyyy-MM-dd hh:mm:ss Z", Locale.getDefault())
    var defaultDateFormat: SimpleDateFormat =
        SimpleDateFormat("d MMM, EEEE, yyyy", Locale.getDefault())

    fun parse(date: String?): Date? {
        return date?.let { serverDateFormat.parse(it) }
    }

    fun format(date: Date?): String? {
        return date?.let { defaultDateFormat.format(it) }
    }

    fun parse(date: Date?): String? {
        return date?.let { defaultDateFormat.format(it) }
    }

}