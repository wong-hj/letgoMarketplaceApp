package com.example.letgo.screens

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.letgo.nav.Routes

@Composable
fun MainScreen() {

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Routes.Login.route) {

        composable(Routes.Login.route) {
            Login(navController = navController)
        }

        composable(Routes.Register.route) {
            Register(navController = navController)
        }

        composable(Routes.HomePage.route) {
            HomePage(navController = navController)
        }

        composable(Routes.Profile.route) {
            Profile(navController = navController)
        }

        composable(Routes.Liked.route) {
            Liked(navController = navController)
        }

        composable(Routes.AddProduct.route) {
            AddProduct(navController = navController)
        }

        composable(Routes.EditProduct.route + "/{productID}") {
            EditProduct(navController = navController)
        }

        composable(Routes.ProductDetails.route + "/{productID}") {
            ProductDetails(navController = navController)
        }

    }
}