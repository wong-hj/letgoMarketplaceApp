package com.example.letgo.viewModel

import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.example.letgo.models.Users
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class RegisterViewModel(): ViewModel() {

    suspend fun signUp(
        email: String,
        password: String,
        name: String,
        university: String,
        studentID: String,
        contact: String,
        context: android.content.Context
    ): Boolean {
        return try {

            Firebase.auth.createUserWithEmailAndPassword(email, password)
                .await()

            val uid = FirebaseAuth.getInstance().currentUser?.uid;

            val db: FirebaseFirestore = FirebaseFirestore.getInstance()

            val dbUsers: DocumentReference = db.collection("Users").document(uid ?: "")

            val userDetails = Users(uid ?: "", name,email,university,studentID, emptyList(), contact)

            dbUsers.set(userDetails).addOnSuccessListener {

            }.addOnFailureListener { e->

                Toast.makeText(
                    context,
                    "Failure \n$e",
                    Toast.LENGTH_SHORT
                ).show()
            }

            true

        } catch (e: Exception) {

            false

        }
    }

}