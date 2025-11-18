package com.nahid.book_orbit.core.utils

sealed class Results<out T> {
    data class Success<T>(val data: T) : Results<T>()
    data class Error(val exception:Exception) : Results<Nothing>()
}