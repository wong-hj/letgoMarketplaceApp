package com.example.letgo.viewModel

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class LoginViewModel(): ViewModel() {

    suspend fun logIn(email: String, password: String): AuthResult? {

        return try {
            val data = Firebase.auth
                .signInWithEmailAndPassword(email, password)
                .await()

            data
        } catch (e: Exception) {
            null
        }
    }

}