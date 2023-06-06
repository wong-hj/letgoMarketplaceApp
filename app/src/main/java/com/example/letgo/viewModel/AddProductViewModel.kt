package com.example.letgo.viewModel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class AddProductViewModel : ViewModel() {

    fun addProduct(image: Uri, name: String, description: String, brand: String, quality: String, location: String, price: String) {

        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.reference.child("images/$name.jpg")

        val uploadTask = storageRef.putFile(image)

        uploadTask.addOnSuccessListener {
            // Image upload successful

            storageRef.downloadUrl.addOnSuccessListener { downloadUrl ->
                val imageUrl = downloadUrl.toString()
                val db = FirebaseFirestore.getInstance()
                val userId = FirebaseAuth.getInstance().currentUser?.uid
                val product = hashMapOf(
                    "name" to name,
                    "description" to description,
                    "brand" to brand,
                    "quality" to quality,
                    "location" to location,
                    "price" to price.toIntOrNull(),
                    "likes" to 0,
                    "userID" to userId,
                    "imageURL" to imageUrl
                )

                db.collection("Products")
                    .add(product)
                    .addOnSuccessListener { documentReference ->
                        db.collection("Products").document(documentReference.id)
                            .update("productID", documentReference.id)
                        Log.d("SuccessAdd", "Product added with ID: ${documentReference.id}")
                    }
            }
        }.addOnFailureListener { exception ->
            // Handle the image upload failure case
            Log.e("ImageUploadError", "Error uploading image", exception)
        }





    }
}