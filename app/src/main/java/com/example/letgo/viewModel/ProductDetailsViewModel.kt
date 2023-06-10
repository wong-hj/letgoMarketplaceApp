package com.example.letgo.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.letgo.models.Products
import com.example.letgo.models.Reviews
import com.example.letgo.models.Users
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ProductDetailsViewModel: ViewModel() {

    private val _product = MutableLiveData<Products>()
    val getData: LiveData<Products> = _product

    private val _user = MutableLiveData<Users>()
    val getUser: LiveData<Users> = _user

    private val _currentUser = MutableLiveData<Users>()
    val getCurrentUser: LiveData<Users> = _currentUser

    private val _reviews = MutableLiveData<List<Reviews>>()
    val reviewsListByProduct: LiveData<List<Reviews>> = _reviews

    fun getUser(userID: String): LiveData<Users> {

        getProductUser(userID)
        return getUser
    }

    fun getData(documentID: String) {
        viewModelScope.launch {
            _product.value = fetchProduct(documentID)
        }
    }

    fun getReviews(productID: String?) {
        viewModelScope.launch {
            _reviews.value = fetchReviewsByProduct(productID)
        }
    }


    suspend fun fetchProduct(documentID: String): Products? {
        val db = FirebaseFirestore.getInstance()
        val productDocRef = db.collection("Products").document(documentID)
        try {
            val documentSnapshot = productDocRef.get().await()
            if (documentSnapshot.exists()) {
                val product = documentSnapshot.toObject<Products>()
                return product

            }
        } catch (e: Exception) {
            Log.e("Firestore", "Error fetching product: ${e.message}")
        }

        return null
    }

    suspend fun productUser(userID: String): Users? {
        val db = FirebaseFirestore.getInstance()
        val userDocRef = db.collection("Users").document(userID)
        try {
            val documentSnapshot = userDocRef.get().await()
            if (documentSnapshot.exists()) {
                val user = documentSnapshot.toObject<Users>()
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

    fun makeOffer(productID: String?, imageURL: String?, offerPrice: String?, productName: String?, sellerID: String?, buyerName: String?, callback: (Boolean) -> Unit) {

        val db = FirebaseFirestore.getInstance()

        val userId = FirebaseAuth.getInstance().currentUser?.uid

        val offer = hashMapOf(
            "offerID" to "",
            "productID" to productID,
            "buyerID" to userId,
            "imageURL" to imageURL,
            "offerPrice" to offerPrice?.toInt(),
            "productName" to productName,
            "sellerID" to sellerID,
            "buyerName" to buyerName,
            "status" to "Pending"
        )

        db.collection("Offers")
            .add(offer)
            .addOnSuccessListener {

                db.collection("Offers").document(it.id).update("offerID", it.id)
                callback(true)

            }.addOnFailureListener {
                // Unlike operation failed
                callback(false)
            }
    }

    fun giveReview(rating: Int?, review: String?, userID: String?, userName: String?, sellerID: String?, offerID: String?, productID: String?, callback: (Boolean) -> Unit) {
        val db = FirebaseFirestore.getInstance()


        val review = hashMapOf(
            "rating" to rating,
            "review" to review,
            "sellerID" to sellerID,
            "userID" to userID,
            "userName" to userName,
            "productID" to productID
        )

        db.collection("Reviews")
            .add(review)
            .addOnSuccessListener {

                db.collection("Reviews").document(it.id).update("reviewID", it.id)

                db.collection("Offers").document(offerID?.trim() ?: "")
                    .delete()

                callback(true)

            }.addOnFailureListener {
                // Unlike operation failed
                callback(false)
            }

    }

    suspend fun fetchReviewsByProduct(productID: String?): List<Reviews> {
        Log.d("TESTING2345", "HERE BRO")
        val db = FirebaseFirestore.getInstance()
        val reviewsList = mutableListOf<Reviews>()

        try {
            val querySnapshot = db.collection("Reviews")
                .whereEqualTo("productID", productID)
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