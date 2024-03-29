package com.example.letgo.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.letgo.models.Offers
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
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

        val db = FirebaseFirestore.getInstance()
        val currentUserID = FirebaseAuth.getInstance().currentUser?.uid
        val offersList = mutableListOf<Offers>()

        try {
            val querySnapshot = db.collection("Offers")
                .whereEqualTo("sellerID", currentUserID)
                .get()
                .await()

            for (document in querySnapshot) {
                val offerID = document.getString("offerID") ?: ""
                val productID = document.getString("productID") ?: ""
                val productName = document.getString("productName") ?: ""
                val sellerID = document.getString("sellerID") ?: ""
                val buyerID = document.getString("buyerID") ?: ""
                val buyerName = document.getString("buyerName") ?: ""
                val offerPrice = document.getLong("offerPrice")?.toInt() ?: 0
                val imageURL = document.getString("imageURL") ?: ""
                val status = document.getString("status") ?: ""

                val offer = Offers(offerID, productID, productName, sellerID, buyerID, buyerName, offerPrice, imageURL, status)
                offersList.add(offer)
            }

        } catch (e: FirebaseFirestoreException) {
            Log.d("Firebase", "Error querying offers: ", e)
        }

        return offersList
    }

    suspend fun fetchBuyerOfferProducts(): List<Offers> {

        val db = FirebaseFirestore.getInstance()
        val currentUserID = FirebaseAuth.getInstance().currentUser?.uid
        val offersList = mutableListOf<Offers>()

        try {
            val querySnapshot = db.collection("Offers")
                .whereEqualTo("buyerID", currentUserID)
                .get()
                .await()

            for (document in querySnapshot) {
                val offerID = document.getString("offerID") ?: ""
                val productID = document.getString("productID") ?: ""
                val productName = document.getString("productName") ?: ""
                val sellerID = document.getString("sellerID") ?: ""
                val buyerID = document.getString("buyerID") ?: ""
                val buyerName = document.getString("buyerName") ?: ""
                val offerPrice = document.getLong("offerPrice")?.toInt() ?: 0
                val imageURL = document.getString("imageURL") ?: ""
                val status = document.getString("status") ?: ""

                val offer = Offers(offerID, productID, productName, sellerID, buyerID, buyerName, offerPrice, imageURL, status)
                offersList.add(offer)


            }

        } catch (e: FirebaseFirestoreException) {
            Log.d("Firebase", "Error querying offers: ", e)
        }

        return offersList
    }

    fun acceptOffer(offerID: String) {
        val db = FirebaseFirestore.getInstance()
        db.collection("Offers").document(offerID.trim())
            .update("status", "Accept")
    }

    fun denyOffer(offerID: String) {

        val db = FirebaseFirestore.getInstance()
        db.collection("Offers").document(offerID.trim())
            .delete()

    }





}


