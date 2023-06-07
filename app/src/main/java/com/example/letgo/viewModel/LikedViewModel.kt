package com.example.letgo.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.letgo.models.Products
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


class LikedViewModel : ViewModel() {

    private val _products = MutableLiveData<List<Products>>()
    val likedProducts: LiveData<List<Products>> = _products

    init {
        getLikedData()
    }

    private fun getLikedData() {
        viewModelScope.launch {
            _products.value = fetchLikedProducts()
        }
    }

    suspend fun fetchLikedProducts(): List<Products> {
        val db = FirebaseFirestore.getInstance()
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        val userDocRef = db.collection("Users").document(userId ?: "")
        val documentSnapshot = userDocRef.get().await()
        val likedProducts = documentSnapshot.get("likedProducts") as? List<String>
        val products = mutableListOf<Products>()
        if (likedProducts != null) {
            // Fetch the corresponding product documents using the IDs in likedProducts
            for (productId in likedProducts) {
                val productDocRef = db.collection("Products").document(productId)
                val productSnapshot = productDocRef.get().await()
                val product = productSnapshot.toObject<Products>()
                if (product != null) {
                    products.add(product)
                }
            }
        }

        return products
    }


}


