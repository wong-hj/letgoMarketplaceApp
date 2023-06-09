package com.example.letgo.viewModel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.letgo.models.Offers
import com.example.letgo.models.Products
import com.example.letgo.models.Reviews
import com.example.letgo.models.Users
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class UserViewModel : ViewModel() {

    val state = mutableStateOf(Users())

    private val _userListings = MutableLiveData<List<Products>>()
    val userListings: LiveData<List<Products>> = _userListings

    private val _isLoading = MutableStateFlow( false )
    val isLoading = _isLoading.asStateFlow()

    private val _reviews = MutableLiveData<List<Reviews>>()
    val reviewsList: LiveData<List<Reviews>> = _reviews

    init {
        getServiceData()
        getReviews()
    }

    fun getServiceData() {
        viewModelScope.launch {
            _isLoading.value = true
            state.value = fetchUser()!!
            _isLoading.value = false
        }
    }

    fun getReviews() {
        viewModelScope.launch {
            _reviews.value = fetchReviews()
        }
    }


    suspend fun fetchUser(): Users? {

        val db = FirebaseFirestore.getInstance()

        val userId = FirebaseAuth.getInstance().currentUser?.uid

        return try {
            val query = db.collection("Users").document(userId ?: "")

            val result = query.get().await()

            if (result.exists()) {
                result.toObject<Users>()
            } else {
                null
            }

        } catch (e: Exception) {
            null
        }

    }

    suspend fun fetchUserListing(): List<Products> {
        val db = FirebaseFirestore.getInstance()
        val userId = FirebaseAuth.getInstance().currentUser?.uid

        try {
            val querySnapshot = db.collection("Products")
                .whereEqualTo("userID", userId ?: "")
                .get().await()


            return querySnapshot.toObjects<Products>()

        } catch (e: FirebaseFirestoreException) {
            Log.d("Firebase", "Error getting documents: ", e)

            return emptyList()
        }
    }

    fun performFetchUserListing() {

        viewModelScope.launch {

            _userListings.value = fetchUserListing()

        }
    }

    suspend fun fetchReviews(): List<Reviews> {
        Log.d("TESTING2345", "HERE BRO")
        val db = FirebaseFirestore.getInstance()
        val currentUserID = FirebaseAuth.getInstance().currentUser?.uid
        val reviewsList = mutableListOf<Reviews>()

        try {
            val querySnapshot = db.collection("Reviews")
                .whereEqualTo("sellerID", currentUserID)
                .get()
                .await()

            for (document in querySnapshot) {
                val productID = document.getString("productID") ?: ""
                val rating = document.getLong("rating")?.toInt() ?: 0
                val reviewID = document.getString("reviewID") ?: ""
                val review = document.getString("review") ?: ""
                val sellerID = document.getString("sellerID") ?: ""
                val userID = document.getString("userID") ?: ""
                val userName = document.getString("userName") ?: ""

                val reviewData = Reviews(rating, review, reviewID, userID, userName, sellerID, productID)

                reviewsList.add(reviewData)


            }

            Log.d("TESTING234", "HERE BRO")
        } catch (e: FirebaseFirestoreException) {
            Log.d("Firebase", "Error querying offers: ", e)
        }

        return reviewsList
    }


}