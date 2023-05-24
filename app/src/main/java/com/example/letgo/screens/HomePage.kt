package com.example.letgo.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.example.letgo.nav.Routes
import com.example.letgo.widgets.CustomBottomBar
import com.example.letgo.widgets.CustomTopBar

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun HomePage(navController: NavHostController) {
    Scaffold(
        topBar = {
            CustomTopBar(
                navController = navController,
                title = "Discover",
                showBackIcon = true
            )
        },
        content = {},
        bottomBar = { CustomBottomBar(navController = navController) }

    )


}