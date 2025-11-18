package com.suffixit.smartadmin.core.utils.extension

import com.nahid.book_orbit.data.remote.dto.ApiError
import com.nahid.book_orbit.core.utils.Logger
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse

private const val TAG = "HttpResponseExt"
suspend fun HttpResponse.getException(): String {
    return try {
        val apiError = this.body<ApiError>()
        Logger.log(TAG, "getException: $apiError")
        apiError.message?: "Something went wrong, please try again later"
    } catch (ex: Exception) {
        Logger.log(TAG, "getException: Exception $ex")
        "Something went wrong, please try again later"
    }
}