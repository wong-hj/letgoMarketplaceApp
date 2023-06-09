package com.example.letgo.models

data class Users(

    var userID: String = "",
    var name: String = "",
    var email: String = "",
    var university: String = "",
    var studentID: String = "",
    var likedProducts: List<String>? = emptyList(),
    var makeOfferProducts: Map<String, Offer>? = null
) {
    data class Offer(
        var offerName: String = "",
        var offerPrice: String = "",
        var productID: String = ""
    )
}
