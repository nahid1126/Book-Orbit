package com.nahid.book_orbit.data.repository

import android.util.Log
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import com.nahid.book_orbit.core.utils.Results
import com.nahid.book_orbit.core.utils.extension.getSpecificException
import com.nahid.book_orbit.data.remote.dto.Gems
import com.nahid.book_orbit.data.remote.dto.GemsTransaction
import com.nahid.book_orbit.domain.repository.GemsRepository
import kotlinx.coroutines.tasks.await

private const val TAG = "GemsRepositoryImpl"
class GemsRepositoryImpl(
    private val db: FirebaseFirestore
) : GemsRepository {
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
            Results.Error(e.getSpecificException())
        }
    }


    override suspend fun purchaseGems(uid: String, gemsId: String): Results<Boolean> {
        return try {
            val gemsDoc = db.collection("gems").document(gemsId).get().await()
            if (!gemsDoc.exists()) return Results.Error(Exception("Gems pack not found"))
            val gemsAmount = gemsDoc.getLong("gemsAmount") ?: 0L
            val walletRef = db.collection("wallet").document(uid)

            db.runTransaction { trx ->
                val snap = trx.get(walletRef)
                val currentGems = snap.getLong("gems") ?: 0L
                val newGems = currentGems + gemsAmount

                trx.set(walletRef, mapOf("gems" to newGems), SetOptions.merge())

                val txnRef = walletRef.collection("transactions").document()
                trx.set(
                    txnRef, mapOf(
                        "gemsAmount" to gemsAmount,
                        "timestamp" to FieldValue.serverTimestamp()
                    )
                )
            }.await()

            Results.Success(true)
        } catch (e: Exception) {
            Results.Error(e.getSpecificException())
        }
    }

    // --------------------
    // Purchase Book (deduct gems)
    // --------------------
    override suspend fun purchaseBook(uid: String, bookId: String): Results<Boolean> {
        return try {
            val bookRef = db.collection("books").document(bookId)
            val bookDoc = bookRef.get().await()
            if (!bookDoc.exists()) return Results.Error(Exception("Book not found"))

            val bookPrice = bookDoc.getLong("price") ?: 0L

            val walletRef = db.collection("wallet").document(uid)
            val purchaseRef = db.collection("purchase").document(uid)

            // Transaction to check wallet and buy book
            db.runTransaction { trx ->
                val walletSnap = trx.get(walletRef)
                val currentGems = walletSnap.getLong("gems") ?: 0L

                if (currentGems < bookPrice) throw Exception("Insufficient gems")

                // Deduct gems
                trx.update(walletRef, "gems", currentGems - bookPrice)

                // Add book purchase
                trx.set(
                    purchaseRef,
                    mapOf("userId" to uid, "books.$bookId" to true),
                    SetOptions.merge()
                )
            }.await()

            Results.Success(true)

        } catch (e: Exception) {
            Results.Error(e.getSpecificException())
        }
    }

    // --------------------
    // Get total gems
    // --------------------


    // --------------------
    // Get transaction history
    // --------------------
    override suspend fun getTransactionHistory(uid: String): Results<List<GemsTransaction>> {
        return try {
            Log.d(TAG, "getTransactionHistory: ")
            val transactions = db.collection("wallet").document(uid)
                .collection("transactions")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .await()
                .toObjects(GemsTransaction::class.java)
            Log.d(TAG, "getTransactionHistory: $transactions")
            Results.Success(transactions)
        } catch (e: Exception) {
            Results.Error(e.getSpecificException())
        }
    }

}