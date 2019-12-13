package com.cognota.core.util

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


object DateTimeUtil {
    const val DEFAULT_DATE_TIME_FORMAT = "yyyy-MM-dd hh:mm:ss"
    const val DEFAULT_DATE_FORMAT = "d MMM, EEEE, yyyy"

    var serverDateFormat: DateFormat =
        SimpleDateFormat("yyyy-MM-dd hh:mm:ss Z", Locale.getDefault())
    var defaultDateFormat: DateFormat =
        SimpleDateFormat("d MMM, EEEE, yyyy", Locale.getDefault())

    fun parse(date: String): Date {
        return serverDateFormat.parse(date)!!
    }

    fun parse(date: Date): String {
        return defaultDateFormat.format(date)
    }

}