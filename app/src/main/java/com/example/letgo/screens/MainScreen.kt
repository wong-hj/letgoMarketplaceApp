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
    }
}