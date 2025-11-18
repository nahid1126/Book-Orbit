package com.nahid.book_orbit.data.remote.dto

data class Book(
    val id: String = "",
    val title: String = "",
    val author: String = "",
    val description: String = "",
    val coverUrl: String = "",
    val pdfUrl: String = "",
    val category: String = "",
    val price: Double = 0.0,
    val isFree: Boolean = false,
    val uploadedAt: Long = System.currentTimeMillis(),
)
