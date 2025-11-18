package com.suffixit.smartadmin.core.utils.extension

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Long.longToSimpleDateFormatString(): String {
    var convertedDate = ""
    try {
        val longDate = Date(this)
        val simpleDateFormat = SimpleDateFormat("dd-MMM-yyyy", Locale.US)
        convertedDate = simpleDateFormat.format(longDate)
    } catch (ex: Exception) {
        ex.printStackTrace()
    }
    return convertedDate
}

fun Long.checkDate(serverDateTimeInMilliSeconds: Long): Boolean {
    val currentDate: Long = this
    val currentDateString = currentDate.longToSimpleDateFormatString()
    val syncDate = serverDateTimeInMilliSeconds.longToSimpleDateFormatString()
    return syncDate.equals(currentDateString, ignoreCase = true)
}