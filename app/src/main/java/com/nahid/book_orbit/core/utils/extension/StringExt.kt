package com.suffixit.smartadmin.core.utils.extension

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

fun String.checkIsAfter(deadline: String): Boolean {

    val currentDate = this

    var initialStartDate = deadline.split("-")
    var startDay = initialStartDate[0]
    var startMonth = initialStartDate[1]
    var startYear = initialStartDate[2]

    if (startDay.length == 1){
        startDay = "0${startDay}"
    }

    if (startMonth.length == 1){
        startMonth = "0${startMonth}"
    }

    val furnishedStartDate = "${startDay}-${startMonth}-${startYear}"

    val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy", Locale.ENGLISH)

    val startDate = LocalDateTime.parse(furnishedStartDate, formatter)
    val oneDayBefore = startDate.minusDays(1)

    val targetTime = LocalDateTime.parse(currentDate, formatter)

    return targetTime.isAfter(oneDayBefore)
}

fun String.checkIsBefore(deadline: String): Boolean {

    val currentDate = this

    var initialBeforeDate = deadline.split("-")
    var beforeDay = initialBeforeDate[0]
    var beforeMonth = initialBeforeDate[1]
    var beforeYear = initialBeforeDate[2]

    if (beforeDay.length == 1){
        beforeDay = "0${beforeDay}"
    }

    if (beforeMonth.length == 1){
        beforeMonth = "0${beforeMonth}"
    }

    val furnishedBeforeDate = "${beforeDay}-${beforeMonth}-${beforeYear}"

    val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy", Locale.ENGLISH)

    val beforeDate = LocalDateTime.parse(furnishedBeforeDate, formatter)
    val oneDayAfter = beforeDate.plusDays(1)

    val targetDate = LocalDateTime.parse(currentDate, formatter)

    return targetDate.isBefore(oneDayAfter)
}

fun String.equalIgnoreCase(other: String): Boolean {
    return this.equals(other, ignoreCase = true)
}

fun String.checkInputType(text: String): Boolean{
    val pattern = Regex("^\\d+\$")

    return if (!text.equalIgnoreCase("ONLY_NUMBER")){
        true
    }else{
        pattern.matches(
            this
        )
    }
}

