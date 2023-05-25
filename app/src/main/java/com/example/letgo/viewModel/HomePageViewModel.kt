package com.example.letgo.viewModel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
//import androidx.compose.ui.tooling.data.EmptyGroup.data
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.letgo.models.Products
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ktx.toObjects
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class HomePageViewModel : ViewModel() {

    private val _products = MutableLiveData<List<Products>>()
    val products: LiveData<List<Products>> = _products

    private val _searchProducts = MutableLiveData<List<Products>>()
    val searchProducts: LiveData<List<Products>> = _searchProducts

    init {
        getProductData()
    }

    private fun getProductData() {
        viewModelScope.launch {
            _products.value = getAllProductsList()
        }
    }

    suspend fun getAllProductsList(): List<Products> {
        val db = FirebaseFirestore.getInstance()

        try {
            val querySnapshot = db.collection("Products").get().await()

            return querySnapshot.toObjects<Products>()
        } catch (e: FirebaseFirestoreException) {
            Log.d("Firebase", "Error getting documents: ", e)

            return emptyList()
        }
    }

    suspend fun getSearchProducts(query: String?): List<Products> {
        val db = FirebaseFirestore.getInstance()

        try {
            val querySnapshot = db.collection("Products")
                .orderBy("name")
                .startAt(query)
                .endAt(query + '\uf8ff')
                .get().await()


            return querySnapshot.toObjects<Products>()

        } catch (e: FirebaseFirestoreException) {
            Log.d("Firebase", "Error getting documents: ", e)

            return emptyList()
        }
    }

    fun performSearch(query: String?) {


        viewModelScope.launch {

            _searchProducts.value = getSearchProducts(query)

        }
    }

    fun clearSearchResults() {
        _searchProducts.value = emptyList()
    }
}