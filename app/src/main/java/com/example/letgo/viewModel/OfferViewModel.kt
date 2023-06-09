package com.example.letgo.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.letgo.models.Offers
import com.example.letgo.models.Products
import com.example.letgo.models.Users
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class OfferViewModel : ViewModel() {

    private val _offers = MutableLiveData<List<Offers>>()
    val offerProducts: LiveData<List<Offers>> = _offers

    private val _buyerOffers = MutableLiveData<List<Offers>>()
    val buyerOfferProducts: LiveData<List<Offers>> = _buyerOffers
    init {
        getOfferData()
        getBuyerOfferData()
    }

    fun getOfferData() {
        viewModelScope.launch {
            _offers.value = fetchOfferProducts()
        }
    }

    fun getBuyerOfferData() {
        viewModelScope.launch {
            _buyerOffers.value = fetchBuyerOfferProducts()
        }
    }

    suspend fun fetchOfferProducts(): List<Offers> {
        Log.d("TESTING2345", "HERE BRO")
        val db = FirebaseFirestore.getInstance()
        val currentUserID = FirebaseAuth.getInstance().currentUser?.uid
        val offersList = mutableListOf<Offers>()

        try {
            val querySnapshot = db.collection("Offers")
                .whereEqualTo("sellerID", currentUserID)
                .get()
                .await()

            for (document in querySnapshot) {
                val productID = document.getString("productID") ?: ""
                val productName = document.getString("productName") ?: ""
                val sellerID = document.getString("sellerID") ?: ""
                val buyerID = document.getString("buyerID") ?: ""
                val userName = document.getString("userName") ?: ""
                val offerPrice = document.getDouble("offerPrice") ?: 0.0
                val imageURL = document.getString("imageURL") ?: ""

                val offer = Offers(productID, productName, sellerID, buyerID, userName, offerPrice, imageURL)
                offersList.add(offer)
            }

            Log.d("TESTING234", "HERE BRO")
        } catch (e: FirebaseFirestoreException) {
            Log.d("Firebase", "Error querying offers: ", e)
        }

        return offersList
    }

    suspend fun fetchBuyerOfferProducts(): List<Offers> {
        Log.d("TESTING2345", "HERE BRO")
        val db = FirebaseFirestore.getInstance()
        val currentUserID = FirebaseAuth.getInstance().currentUser?.uid
        val offersList = mutableListOf<Offers>()

        try {
            val querySnapshot = db.collection("Offers")
                .whereEqualTo("buyerID", currentUserID)
                .get()
                .await()

            for (document in querySnapshot) {
                val productID = document.getString("productID") ?: ""
                val productName = document.getString("productName") ?: ""
                val sellerID = document.getString("sellerID") ?: ""
                val buyerID = document.getString("buyerID") ?: ""
                val userName = document.getString("userName") ?: ""
                val offerPrice = document.getDouble("offerPrice") ?: 0.0
                val imageURL = document.getString("imageURL") ?: ""

                val offer = Offers(productID, productName, sellerID, buyerID, userName, offerPrice, imageURL)
                offersList.add(offer)
            }

            Log.d("TESTING234", "HERE BRO")
        } catch (e: FirebaseFirestoreException) {
            Log.d("Firebase", "Error querying offers: ", e)
        }

        return offersList
    }





}


