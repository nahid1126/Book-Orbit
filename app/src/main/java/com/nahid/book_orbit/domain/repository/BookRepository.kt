package com.nahid.book_orbit.domain.repository

import com.nahid.book_orbit.core.utils.Results
import com.nahid.book_orbit.data.remote.dto.Book

interface BookRepository {
    suspend fun getAllBooks(): Results<List<Book>>
    suspend fun isBookPurchased(userId: String, bookId: String): Boolean
}