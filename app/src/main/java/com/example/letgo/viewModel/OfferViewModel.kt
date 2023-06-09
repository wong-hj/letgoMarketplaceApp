package com.example.letgo.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    private val _users = MutableLiveData<List<Users>>()
    val offerProducts: LiveData<List<Users>> = _users

    init {
        getOfferData()
    }

    private fun getOfferData() {
        viewModelScope.launch {
            _users.value = fetchOfferProducts()
        }
    }

//    suspend fun fetchOfferProducts(): List<Users>? {
//
//        val db = FirebaseFirestore.getInstance()
//        val userId = FirebaseAuth.getInstance().currentUser?.uid
//
//        val userDocRef = db.collection("Users").document(userId ?: "")
//
//        val documentSnapshot = userDocRef.get().await()
//
//        val offerProductsMap = documentSnapshot.get("makeOfferProducts") as? Map<String, Any>
//
//        return offerProductsMap?.map { (_, value) ->
//
//            val offerMap = value as? Map<String, String>
//
//            Users.Offer(
//                offerName = offerMap?.get("offerName") ?: "",
//                offerPrice = offerMap?.get("offerPrice") ?: "",
//                productID = offerMap?.get("productID") ?: ""
//            )
//        }
//    }

    suspend fun fetchOfferProducts(): List<Users> {

//        val db = FirebaseFirestore.getInstance()
//        val userId = FirebaseAuth.getInstance().currentUser?.uid
//
//        val userDocRef = db.collection("Users").document(userId ?: "")
//
//        val documentSnapshot = userDocRef.get().await()
//
//        return documentSnapshot.toObjects<Users>()

        val db = FirebaseFirestore.getInstance()

        val userId = FirebaseAuth.getInstance().currentUser?.uid

        try {
            val querySnapshot = db.collection("Users").document(userId ?: "").get().await()
            val user = querySnapshot.toObject(Users::class.java)

            //return listOf(user)

            return querySnapshot.toObject<Users>()

        } catch (e: FirebaseFirestoreException) {
            Log.d("Firebase", "Error getting documents: ", e)

            return emptyList()
        }
    }




}


