package com.jmlb0003.itcv.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object DateExtensions {

    const val BACKEND_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS"
    private const val SHORT_DATE_FORMAT = "dd - MMM - yyyy"

    private val shortDateFormat get() = SimpleDateFormat(SHORT_DATE_FORMAT, Locale.getDefault())

    fun Date.toShortDateString(): String = shortDateFormat.format(this)

    fun Long.isOlderThanYesterday(): Boolean {
        val yesterday = Calendar.getInstance(Locale.getDefault()).apply {
            add(Calendar.HOUR, -24)
        }
        val thisDate = Calendar.getInstance(Locale.getDefault()).apply {
            timeInMillis = this@isOlderThanYesterday
        }
        return thisDate < yesterday
    }

}
