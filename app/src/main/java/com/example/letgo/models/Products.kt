package com.example.letgo.models

data class Products(
    var productID: String = "",
    var name: String = "",
    var likes: Int = 0,
    var description: String = "",
    var brand: String = "",
    var quality: String = "",
    var location: String = "",
    var price: Int = 0,
    var imageURL: String = "",
    var userID: String = ""
)
