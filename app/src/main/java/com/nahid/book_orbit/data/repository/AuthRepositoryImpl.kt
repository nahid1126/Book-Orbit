package com.nahid.book_orbit.data.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.nahid.book_orbit.core.utils.Results
import com.nahid.book_orbit.core.utils.extension.getSpecificException
import com.nahid.book_orbit.domain.repository.AuthRepository
import kotlinx.coroutines.tasks.await

private const val TAG = "AuthRepositoryImpl"

class AuthRepositoryImpl(
    private val auth: FirebaseAuth,
) : AuthRepository {

    override suspend fun signInWithGoogle(idToken: String): Results<FirebaseUser?> {
        return try {
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            Log.d(TAG, "signInWithGoogle: $credential")
            val result = auth.signInWithCredential(credential).await()
            if (result.user != null) {
                Log.d(TAG, "signInWithGoogle: ${result.user!!.uid}")
                Results.Success(result.user)
            } else {
                Results.Error(Exception("Authentication Failed"))
            }
        } catch (e: Exception) {
            Log.d(TAG, "signInWithGoogle: ${e.getSpecificException()}")
            Results.Error(e.getSpecificException())
        }
    }

}
