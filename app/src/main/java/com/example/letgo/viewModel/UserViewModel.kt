package com.example.letgo.viewModel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.letgo.models.Products
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

    init {
        getServiceData()
    }

    fun getServiceData() {
        viewModelScope.launch {
            _isLoading.value = true
            state.value = fetchUser()!!
            _isLoading.value = false
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
}