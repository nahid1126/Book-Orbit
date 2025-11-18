package com.nahid.book_orbit.domain.repository

import com.nahid.book_orbit.data.remote.dto.Book

interface BookRepository {
    suspend fun getAllBooks(): List<Book>
    suspend fun isBookPurchased(userId: String, bookId: String): Boolean
}