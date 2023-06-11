package com.example.letgo.viewModel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.letgo.models.Products
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


class EditProductViewModel : ViewModel() {

    private val _product = MutableLiveData<Products>()
    val getProduct: LiveData<Products> = _product

    fun getProduct(documentID: String): LiveData<Products> {
        getData(documentID)
        return getProduct
    }

    private fun getData(documentID: String) {
        viewModelScope.launch {
            _product.value = fetchProduct(documentID)
        }
    }
    fun deleteProduct(documentID: String) {

        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.reference
        val imageRef = storageRef.child("images/$documentID.jpg")

        imageRef.delete()
            .addOnSuccessListener {
                val db = FirebaseFirestore.getInstance()
                val productRef = db.collection("Products").document(documentID)

                productRef.delete()
                    .addOnSuccessListener {
                        // Delete successful, perform any additional actions if needed
                        db.collection("Offers")
                            .whereEqualTo("productID", documentID)
                            .get()
                            .addOnSuccessListener { querySnapshot ->
                                for (document in querySnapshot.documents) {
                                    val documentRef = db.collection("Offers").document(document.id)
                                    documentRef.delete()
                                        .addOnSuccessListener {
                                            Log.d("Delete", "Delete successfully")
                                        }
                                        .addOnFailureListener { exception ->
                                            Log.e("Delete", "Error updating review", exception)
                                        }
                                }
                            }
                            .addOnFailureListener { exception ->
                                Log.e("QueryReviews", "Error querying reviews", exception)
                            }

                        db.collection("Reviews")
                            .whereEqualTo("productID", documentID)
                            .get()
                            .addOnSuccessListener { querySnapshot ->
                                for (document in querySnapshot.documents) {
                                    val documentRef = db.collection("Reviews").document(document.id)
                                    documentRef.delete()
                                        .addOnSuccessListener {
                                            Log.d("Delete1", "Delete successfully")
                                        }
                                        .addOnFailureListener { exception ->
                                            Log.e("Delete1", "Error updating review", exception)
                                        }
                                }
                            }
                            .addOnFailureListener { exception ->
                                Log.e("QueryReviews", "Error querying reviews", exception)
                            }

                        val usersCollectionRef = db.collection("Users")
                        usersCollectionRef.get()
                            .addOnSuccessListener { querySnapshot ->

                            val batch = db.batch()
                            for (userDoc in querySnapshot.documents) {
                                // Remove the product ID from the likedProducts array field
                                val userRef = usersCollectionRef.document(userDoc.id)
                                batch.update(userRef, "likedProducts", FieldValue.arrayRemove(documentID))
                            }

                            batch.commit()
                                .addOnSuccessListener {
                                    // The product ID was successfully removed from all users' likedProducts arrays
                                    Log.d("RemoveProduct", "Product removed from all users successfully")
                                }
                                .addOnFailureListener { exception ->
                                    // An error occurred while removing the product ID from users' likedProducts arrays
                                    Log.e("RemoveProduct", "Error removing product from users", exception)
                                }

                        }

                    }
                    .addOnFailureListener { e ->
                        // An error occurred while deleting the document
                    }
            }
            .addOnFailureListener { e ->
                // An error occurred while deleting the image
            }

    }


    fun updateProduct(image: Uri?, name: String, description: String, brand: String, quality: String, location: String, price: String, productID: String) {

        if (image != null) {
            val storage = FirebaseStorage.getInstance()
            val storageRef = storage.reference.child("images/$productID.jpg")

            val uploadTask = storageRef.putFile(image)

            uploadTask.addOnSuccessListener {
                // Image upload successful

                storageRef.downloadUrl.addOnSuccessListener { downloadUrl ->
                    val imageUrl = downloadUrl.toString()
                    val db = FirebaseFirestore.getInstance()
                    val product = hashMapOf(
                        "name" to name,
                        "description" to description,
                        "brand" to brand,
                        "quality" to quality,
                        "location" to location,
                        "price" to price.toIntOrNull(),
                        "imageURL" to imageUrl
                    )

                    db.collection("Products").document(productID)
                        .update(product as Map<String, Any>)
                        .addOnSuccessListener { documentReference ->

                            db.collection("Offers")
                                .whereEqualTo("productID", productID)
                                .get()
                                .addOnSuccessListener { querySnapshot ->
                                    for (document in querySnapshot.documents) {
                                        val documentRef = db.collection("Offers").document(document.id)
                                        documentRef.update("productName", name, "imageURL", imageUrl)
                                            .addOnSuccessListener {
                                                Log.d("Update", "Update successfully")
                                            }
                                            .addOnFailureListener { exception ->
                                                Log.e("Update", "Error updating review", exception)
                                            }
                                    }
                                }
                                .addOnFailureListener { exception ->
                                    Log.e("QueryReviews", "Error querying reviews", exception)
                                }

                            Log.d("SuccessEdit", "Product added with ID: $documentReference")
                        }
                }
            }.addOnFailureListener { exception ->
                // Handle the image upload failure case
                Log.e("ImageUploadError", "Error uploading image", exception)
            }
        } else {
            val db = FirebaseFirestore.getInstance()
            val product = hashMapOf(
                "name" to name,
                "description" to description,
                "brand" to brand,
                "quality" to quality,
                "location" to location,
                "price" to price.toIntOrNull()
            )

            db.collection("Products").document(productID)
                .update(product as Map<String, Any>)
                .addOnSuccessListener { documentReference ->

                    db.collection("Offers")
                        .whereEqualTo("productID", productID)
                        .get()
                        .addOnSuccessListener { querySnapshot ->
                            for (document in querySnapshot.documents) {
                                val documentRef = db.collection("Offers").document(document.id)
                                documentRef.update("productName", name, )
                                    .addOnSuccessListener {
                                        Log.d("Update", "Update successfully")
                                    }
                                    .addOnFailureListener { exception ->
                                        Log.e("Update", "Error updating review", exception)
                                    }
                            }
                        }
                        .addOnFailureListener { exception ->
                            Log.e("QueryReviews", "Error querying reviews", exception)
                        }

                    Log.d("SuccessEdit", "Product added with ID: $documentReference")
                }
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

}




