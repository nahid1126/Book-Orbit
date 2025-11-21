package com.nahid.book_orbit.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.nahid.book_orbit.core.utils.Results
import com.nahid.book_orbit.data.remote.dto.Book
import com.nahid.book_orbit.data.remote.dto.Gems
import com.nahid.book_orbit.domain.repository.GemsRepository
import kotlinx.coroutines.tasks.await

class GemsRepositoryImpl(
    private val db: FirebaseFirestore): GemsRepository {
    override suspend fun getAllGems(): Results<List<Gems>> {
        return try {
            val querySnapshot = db.collection("gems")
                .get()
                .await()
            val gems = querySnapshot.documents.mapNotNull { document ->
                val gem = document.toObject(Gems::class.java)
                gem?.apply {
                    id = document.id
                }
            }
            Results.Success(gems)
        } catch (e: Exception) {
            Results.Error(e)
        }
    }
}