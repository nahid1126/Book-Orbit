package com.nahid.book_orbit.core.utils

sealed class Results<out T> {
    data class Success<T>(val data: T) : Results<T>()
    data class Error(val exception:Exception) : Results<Nothing>()
}

sealed interface PurchaseResult {
    data object Success : PurchaseResult
    data object Pending : PurchaseResult
    data object Cancelled : PurchaseResult
    data object Consumed : PurchaseResult
    data class Error(val message: String) : PurchaseResult
}