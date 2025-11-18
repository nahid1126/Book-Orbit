package com.suffixit.smartadmin.core.utils.extension

import android.annotation.SuppressLint

@SuppressLint("DefaultLocale")
fun Double.toTwoDecimal(): Double {
    return  (this * 100).toInt() / 100.0
}

@SuppressLint("DefaultLocale")
fun Double.toOneDecimal(): Double {
    return  (this * 10).toInt() / 10.0
}