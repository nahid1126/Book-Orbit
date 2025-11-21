package com.nahid.book_orbit.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class Book(
    var id: String = "",
    val title: String = "",
    val writer: String = "",
    val coverImage: String = "",
    val pdfUrl: String = "",
    val price: Int = 0,
    val isFree: Boolean = false,
    val users: List<String?>? = emptyList(),
    val uploadedAt: Long = System.currentTimeMillis(),
    val shortDesc: String = ""
)
