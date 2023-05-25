package com.example.letgo.screens

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Search
//import androidx.compose.material.Scaffold
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.letgo.R
import com.example.letgo.models.Products
import com.example.letgo.nav.Routes
import com.example.letgo.ui.theme.Typography
import com.example.letgo.viewModel.HomePageViewModel
import com.example.letgo.widgets.CustomBottomBar
import com.example.letgo.widgets.CustomHeader
import com.example.letgo.widgets.CustomTopBar
import com.google.android.play.integrity.internal.c
import kotlin.text.Typography


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun HomePage(navController: NavHostController, productVM: HomePageViewModel = viewModel()) {


    val products: List<Products> by productVM.products.observeAsState(emptyList())

    var searchText by remember { mutableStateOf("") }
    var active by remember { mutableStateOf(false) }
    

    Scaffold(

        content = {
            Column(modifier = Modifier.fillMaxWidth()) {
                CustomHeader(value = "Discover")

                SearchBar(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally),
                    query = searchText,
                    onQueryChange = {
                        searchText = it
                    },
                    onSearch = {
                        active = false
                    },
                    active = active,
                    onActiveChange = {
                        active = it
                    },
                    placeholder = {
                        Text(text = "Search")
                    },
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
                    },
                    trailingIcon = {
                        if(active) {
                            Icon(
                                modifier = Modifier.clickable {
                                    if(searchText.isNotEmpty()) {
                                        searchText = ""
                                    } else {
                                        active = false
                                    }

                                },
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close"
                            )
                        }

                    }
                ) {

                }

                Spacer(modifier = Modifier.height(20.dp))

                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(start = 20.dp, end = 20.dp, bottom = 70.dp),
                ) {

                    items(products) { product ->
                        Card(
                            onClick = { /* Do something */ },
                            modifier = Modifier
                                .size(width = 180.dp, height = 200.dp)
                                .align(Alignment.CenterHorizontally)
                        ) {

                            Image(
                                painter = painterResource(id = R.drawable.buzz),
                                contentDescription = "buzz",
                                contentScale = ContentScale.FillWidth,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(140.dp)

                            )

                            val text = buildAnnotatedString {
                                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                    append(product.name)
                                }
                                append("\nRM ${product.price} | ${product.quality}")
                            }

                            Box(
                                modifier = Modifier
                                    .fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = text,
                                    modifier = Modifier.align(Alignment.Center),
                                    textAlign = TextAlign.Center,
                                    style = Typography.body2

                                )
                            }
                        }
                    }
                }


            }


        },
        bottomBar = {
            CustomBottomBar(navController = navController)
        }
    )

        //}

    //)


}


