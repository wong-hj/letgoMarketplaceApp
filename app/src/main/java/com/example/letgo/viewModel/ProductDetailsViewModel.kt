package com.example.letgo.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.letgo.models.Products
import com.example.letgo.models.Users
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ProductDetailsViewModel: ViewModel() {

    private val _product = MutableLiveData<Products>()
    //val getProduct: LiveData<Products> = _product
    val getData: LiveData<Products> = _product

    private val _user = MutableLiveData<Users>()
    val getUser: LiveData<Users> = _user

    private val _currentUser = MutableLiveData<Users>()
    val getCurrentUser: LiveData<Users> = _currentUser

//    fun getProduct(documentID: String): LiveData<Products> {
//        getData(documentID)
//        Log.d("Firestore", getProduct.toString())
//        return getProduct
//    }

    fun getUser(userID: String): LiveData<Users> {

        getProductUser(userID)
        return getUser
    }

    fun getData(documentID: String) {
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

    suspend fun currentUser(): Users? {

        val db = FirebaseFirestore.getInstance()
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        val userDocRef = db.collection("Users").document(userId ?: "")

        try {
            val documentSnapshot = userDocRef.get().await()

            if (documentSnapshot.exists()) {
                val currentUser = documentSnapshot.toObject<Users>()

                return currentUser

            }
        } catch (e: Exception) {
            Log.e("Firestore", "Error fetching user: ${e.message}")
        }

        return null
    }

    fun getCurrentUser() {
        viewModelScope.launch {
            _currentUser.value = currentUser()
        }
    }

    fun addLikeProduct(productID: String, callback: (Boolean) -> Unit) {

        val db = FirebaseFirestore.getInstance()

        val userId = FirebaseAuth.getInstance().currentUser?.uid

        db.collection("Users").document(userId ?: "")
            .update("likedProducts", FieldValue.arrayUnion(productID))
            .addOnSuccessListener {
                db.collection("Products").document(productID)
                    .update("likes", FieldValue.increment(1))
                callback(true)
            }.addOnFailureListener {
                callback(false)
            }


    }

    fun unlikeProduct(productID: String, likes: Int, callback: (Boolean) -> Unit) {

        Log.d("unlikeProduct", likes.toString())

        val db = FirebaseFirestore.getInstance()

        val userId = FirebaseAuth.getInstance().currentUser?.uid

        db.collection("Users").document(userId ?: "")
            .update("likedProducts", FieldValue.arrayRemove(productID))
            .addOnSuccessListener {

                if (likes > 0) {
                    db.collection("Products").document(productID)
                        .update("likes", FieldValue.increment(-1))
                }

                callback(true)

            }.addOnFailureListener {
                // Unlike operation failed
                callback(false)
            }
    }

    fun makeOffer(productID: String?, imageURL: String?, offerPrice: String?, productName: String?, sellerID: String?, userName: String?, callback: (Boolean) -> Unit) {

        val db = FirebaseFirestore.getInstance()

        val userId = FirebaseAuth.getInstance().currentUser?.uid

        val offer = hashMapOf(
            "productID" to productID,
            "buyerID" to userId,
            "imageURL" to imageURL,
            "offerPrice" to offerPrice?.toInt(),
            "productName" to productName,
            "sellerID" to sellerID,
            "userName" to userName
        )

        db.collection("Offers")
            .add(offer)
            .addOnSuccessListener {

                callback(true)

            }.addOnFailureListener {
                // Unlike operation failed
                callback(false)
            }
    }
}