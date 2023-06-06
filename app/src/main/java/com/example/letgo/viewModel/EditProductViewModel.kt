package com.example.letgo.viewModel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.letgo.models.Products
import com.google.firebase.auth.FirebaseAuth
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

    fun deleteProduct(documentID: String, name: String) {

        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.reference
        val imageRef = storageRef.child("images/$name.jpg") // Replace with your image URL or path

        imageRef.delete()
            .addOnSuccessListener {
                val db = FirebaseFirestore.getInstance()
                val productRef = db.collection("Products").document(documentID)

                productRef.delete()
                    .addOnSuccessListener {
                        // Delete successful, perform any additional actions if needed
                        // For example, show a success message or navigate to a different screen
                    }
                    .addOnFailureListener { e ->
                        // An error occurred while deleting the document
                        // Handle the error appropriately, for example, show an error message
                    }
            }
            .addOnFailureListener { e ->
                // An error occurred while deleting the image
                // Handle the error appropriately
            }

    }

//    fun updateProduct(
//        image: Uri,
//        name: String,
//        description: String,
//        brand: String,
//        quality: String,
//        location: String,
//        price: String,
//        productID: String
//    ) {
//        val storage = FirebaseStorage.getInstance()
//        val storageRef = storage.reference.child("images/$name.jpg")
//
//        val uploadTask = storageRef.putFile(image)
//
//        uploadTask.addOnSuccessListener {
//            storageRef.downloadUrl.addOnSuccessListener { downloadUrl ->
//                val imageUrl = downloadUrl.toString()
//                val db = FirebaseFirestore.getInstance()
//                val productRef = db.collection("Products").document(productID)
//
//                val updatedProduct = hashMapOf(
//                    "name" to name,
//                    "description" to description,
//                    "brand" to brand,
//                    "quality" to quality,
//                    "location" to location,
//                    "price" to price.toIntOrNull(),
//                    "imageURL" to imageUrl
//                )
//
//                productRef.update(updatedProduct)
//                    .addOnSuccessListener {
//                        // Product update successful
//                    }
//                    .addOnFailureListener { e ->
//                        // An error occurred while updating the product
//                        // Handle the error appropriately
//                    }
//            }
//        }
//    }

    fun updateProduct(image: Uri, name: String, description: String, brand: String, quality: String, location: String, price: String, productID: String) {

        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.reference.child("images/$name.jpg")

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

                        Log.d("SuccessEdit", "Product added with ID: $documentReference")
                    }
            }
        }.addOnFailureListener { exception ->
            // Handle the image upload failure case
            Log.e("ImageUploadError", "Error uploading image", exception)
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


