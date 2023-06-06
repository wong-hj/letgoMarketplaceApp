package com.example.letgo

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material3.*
import androidx.compose.runtime.*
import com.example.letgo.screens.AddProduct
import com.example.letgo.ui.theme.LetgoTheme
import com.example.letgo.screens.MainScreen
import com.example.letgo.viewModel.HomePageViewModel


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LetgoTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    //AddProduct()
                    //Login()
                    //Register()
                    MainScreen()
                }
            }
        }
    }

}
@OptIn(ExperimentalMaterial3Api::class)
//@Composable

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DefaultPreview() {
    LetgoTheme {
        //Login()
        //Register()
        //AddProduct()
        MainScreen()
    }
}