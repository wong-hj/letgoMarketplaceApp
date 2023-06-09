package com.example.letgo.models

data class Reviews(
    var rating: Int = 0,
    var review: String = "",
    var reviewID: String = "",
    var userID: String = "",
    var userName: String = "",
    var sellerID: String = "",
    var productID: String = ""
)
