package com.nahid.book_orbit.core.utils

import android.util.Log

private const val TAG = "Logger"

object Logger {
    fun log(tag: String = TAG, message: String?){
        if (AppConstants.ENABLE_LOG){
            Log.d(tag, "$message")
        }
    }
}