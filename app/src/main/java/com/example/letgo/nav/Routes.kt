package com.example.letgo.nav

sealed class Routes(val route: String) {

    object Login : Routes("Login")
    object Register : Routes("Register")
    object HomePage: Routes("HomePage")
    object Liked: Routes("Liked")
    object Profile: Routes("Profile")
    object AddProduct: Routes("AddProduct")
    object EditProduct: Routes("EditProduct")
    object ProductDetails: Routes("ProductDetails")
    object Offer: Routes("Offer")
    object EditUser: Routes("EditUser")
}
