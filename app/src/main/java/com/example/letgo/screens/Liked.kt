package com.example.letgo.screens

import android.annotation.SuppressLint
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.example.letgo.widgets.CustomBottomBar
import com.example.letgo.widgets.CustomHeader

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun Liked(navController : NavHostController) {
    Scaffold(

        content = {
            CustomHeader(value = "Liked")
        },
        bottomBar = {
            CustomBottomBar(navController = navController)
        }
    )
}