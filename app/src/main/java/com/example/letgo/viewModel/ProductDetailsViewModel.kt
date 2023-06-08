package com.example.letgo.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.letgo.models.Products
import com.example.letgo.models.Users
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ProductDetailsViewModel: ViewModel() {

    private val _product = MutableLiveData<Products>()
    val getProduct: LiveData<Products> = _product

    private val _user = MutableLiveData<Users>()
    val getUser: LiveData<Users> = _user
    fun getProduct(documentID: String): LiveData<Products> {
        getData(documentID)
        Log.d("Firestore", getProduct.toString())
        return getProduct
    }

    fun getUser(userID: String): LiveData<Users> {

        getProductUser(userID)
        return getUser
    }

    private fun getData(documentID: String) {
        viewModelScope.launch {
            _product.value = fetchProduct(documentID)
            Log.d("Firestore123", _product.value.toString())
        }
    }




    suspend fun fetchProduct(documentID: String): Products? {
        Log.d("Firestore", "Gets here")
        val db = FirebaseFirestore.getInstance()
        val productDocRef = db.collection("Products").document(documentID)
        Log.d("Firestore", "Gets  1")
        try {
            val documentSnapshot = productDocRef.get().await()
            Log.d("Firestore", "Gets here2")
            if (documentSnapshot.exists()) {
                Log.d("Firestore", "Gets here3")
                val product = documentSnapshot.toObject<Products>()
                Log.d("Firestore", product.toString())
                return product

            }
        } catch (e: Exception) {
            Log.e("Firestore", "Error fetching product: ${e.message}")
        }

        return null
    }

    suspend fun productUser(userID: String): Users? {
        Log.d("Firestore", "Gets here111")
        Log.d("Firestore", "Gets here111 - ${userID} - 222")
        val db = FirebaseFirestore.getInstance()
        val userDocRef = db.collection("Users").document(userID)
        Log.d("Firestore", "Gets here222")
        try {
            val documentSnapshot = userDocRef.get().await()
            Log.d("Firestore", "Gets here3333")
            if (documentSnapshot.exists()) {
                val user = documentSnapshot.toObject<Users>()
                Log.d("Firestore", user.toString())
                return user

            }
        } catch (e: Exception) {
            Log.e("Firestore", "Error fetching user: ${e.message}")
        }

        return null
    }

    fun getProductUser(userID: String) {
        viewModelScope.launch {
            _user.value = productUser(userID)
        }
    }
}