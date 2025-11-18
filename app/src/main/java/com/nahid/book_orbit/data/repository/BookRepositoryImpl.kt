package com.nahid.book_orbit.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.nahid.book_orbit.data.remote.dto.Book
import com.nahid.book_orbit.domain.repository.BookRepository
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class BookRepositoryImpl(
    private val db: FirebaseFirestore,
    private val auth: FirebaseAuth
):BookRepository {
    override suspend fun getAllBooks(): List<Book> = suspendCoroutine { cont ->
        db.collection("books").get()
            .addOnSuccessListener { snap ->
                val list = snap.documents.mapNotNull { it.toObject(Book::class.java) }
                cont.resume(list)
            }
            .addOnFailureListener { cont.resume(emptyList()) }
    }

    override suspend fun isBookPurchased(userId: String, bookId: String): Boolean =
        suspendCoroutine { cont ->
            db.collection("users")
                .document(userId)
                .collection("purchases")
                .document(bookId)
                .get()
                .addOnSuccessListener { cont.resume(it.exists()) }
                .addOnFailureListener { cont.resume(false) }
        }
}