package com.example.letgo.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.letgo.models.Users
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class EditUserViewModel : ViewModel() {

    private val _user = MutableLiveData<Users>()
    val getUser: LiveData<Users> = _user

    init {
        getData()
    }

    fun getData() {
        viewModelScope.launch {
            _user.value = fetchUser()
        }
    }

    suspend fun fetchUser(): Users? {
        val db = FirebaseFirestore.getInstance()
        val uid = FirebaseAuth.getInstance().currentUser?.uid;
        val userDocRef = db.collection("Users").document(uid ?: "")

        try {
            val documentSnapshot = userDocRef.get().await()
            if (documentSnapshot.exists()) {
                val user = documentSnapshot.toObject<Users>()
                return user
            }
        } catch (e: Exception) {
            Log.e("Firestore", "Error fetching product: ${e.message}")
        }

        return null
    }

    fun updateUser(email: String, fullName: String, university: String, studentID: String, contact: String, password: String): Boolean {

        val userAuth = FirebaseAuth.getInstance().currentUser

        if (password.isNotBlank()) {

            userAuth?.updatePassword(password)
                ?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d("PasswordUpdate", "Password updated successfully")
                    } else {
                        val exception = task.exception
                        Log.e("PasswordUpdate", "Error updating password", exception)
                    }
                }
        }

        if (email.isNotBlank()) {
            userAuth?.updateEmail(email)
                ?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d("EmailUpdate", "Email updated successfully")
                    } else {
                        val exception = task.exception
                        Log.e("EmailUpdate", "Error updating email", exception)
                    }
                }
        }


        val db = FirebaseFirestore.getInstance()
        val uid = FirebaseAuth.getInstance().currentUser?.uid;
        val user = hashMapOf<String, Any?>(
            "email" to email,
            "name" to fullName,
            "university" to university,
            "studentID" to studentID,
            "contact" to contact
        )

        db.collection("Users").document(uid ?: "")
            .set(user, SetOptions.merge())
            .addOnSuccessListener { documentReference ->

                db.collection("Reviews")
                    .whereEqualTo("userID", uid)
                    .get()
                    .addOnSuccessListener { querySnapshot ->
                        for (document in querySnapshot.documents) {
                            val documentRef = db.collection("Reviews").document(document.id)
                            documentRef.update("userName", fullName)
                                .addOnSuccessListener {
                                    Log.d("UpdateReview", "Review updated successfully")
                                }
                                .addOnFailureListener { exception ->
                                    Log.e("UpdateReview", "Error updating review", exception)
                                }
                        }
                    }
                    .addOnFailureListener { exception ->
                        Log.e("QueryReviews", "Error querying reviews", exception)
                    }

                db.collection("Offers")
                    .whereEqualTo("buyerID", uid)
                    .get()
                    .addOnSuccessListener { querySnapshot ->
                        for (document in querySnapshot.documents) {
                            val documentRef = db.collection("Offers").document(document.id)
                            documentRef.update("buyerName", fullName)
                                .addOnSuccessListener {
                                    Log.d("UpdateReview", "Review updated successfully")
                                }
                                .addOnFailureListener { exception ->
                                    Log.e("UpdateReview", "Error updating review", exception)
                                }
                        }
                    }
                    .addOnFailureListener { exception ->
                        Log.e("QueryReviews", "Error querying reviews", exception)
                    }


                Log.d("SuccessEdit", "Product added with ID: $documentReference")
            }

        return true
    }


}