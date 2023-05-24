package com.example.letgo.nav

sealed class Routes(val route: String) {

    object Login : Routes("Login")
    object Register : Routes("Register")
    object HomePage: Routes("HomePage")
}
