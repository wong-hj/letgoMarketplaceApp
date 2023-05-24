package com.example.letgo.viewModel

import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class LoginViewModel(): ViewModel() {

    // Variables
//    var email by mutableStateOf("")
//    var password by mutableStateOf("")

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