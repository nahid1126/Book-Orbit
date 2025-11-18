package com.nahid.book_orbit.data.di

import android.util.Log
import com.nahid.book_orbit.core.remote.ServerConstants
import io.ktor.client.HttpClient
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import org.koin.dsl.module

private const val TAG = "NetworkModule"

@OptIn(ExperimentalSerializationApi::class)
val networkModule = module {
    single {
        HttpClient {
            install(ContentNegotiation) {
                json(
                    Json {
                        prettyPrint = true
                        isLenient = true
                        explicitNulls = false
                        ignoreUnknownKeys = true
                    }
                )
            }

            install(HttpTimeout) {
                requestTimeoutMillis = ServerConstants.REQUEST_TIMEOUT * 1000
                connectTimeoutMillis = ServerConstants.CONNECTION_TIMEOUT * 1000
                socketTimeoutMillis = ServerConstants.SOCKET_TIMEOUT * 1000
            }

            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        Log.d(TAG, "NETWORK_LOG : $message")
                    }
                }
                level = LogLevel.BODY
            }
            install(DefaultRequest) {
                header(HttpHeaders.ContentType, ContentType.Application.Json)

            }
        }
    }
}