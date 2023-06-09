package com.example.letgo.models

data class Offers(

    val offerID: String = "",
    val productID: String = "",
    val productName: String = "",
    val sellerID: String = "",
    val buyerID: String = "",
    val buyerName: String = "",
    val offerPrice: Int = 0,
    val imageURL: String = "",
    val status: String = "None"

)
