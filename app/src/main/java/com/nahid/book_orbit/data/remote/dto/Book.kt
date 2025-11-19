package com.nahid.book_orbit.data.remote.dto

data class Book(
    val id: String = "",
    val title: String = "",
    val writer: String = "",
    val coverImage: String = "",
    val pdfUrl: String = "",
    val price: Int = 0,
    val isFree: Boolean = false,
    val users: List<String>? = null,
    val uploadedAt: Long = System.currentTimeMillis(),
)
