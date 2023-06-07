package com.example.letgo.screens

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.letgo.R
import com.example.letgo.models.Products
import com.example.letgo.nav.Routes
import com.example.letgo.ui.theme.Typography
import com.example.letgo.viewModel.HomePageViewModel
import com.example.letgo.widgets.CustomBottomBar
import com.example.letgo.widgets.CustomHeader
import androidx.lifecycle.viewModelScope
import com.example.letgo.viewModel.LikedViewModel
import com.example.letgo.viewModel.UserViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun HomePage(navController: NavHostController, productVM: HomePageViewModel = viewModel(), userVM: UserViewModel = viewModel()) {

    val isLoading by productVM.isLoading.collectAsState()
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = isLoading)


    val products: List<Products> by productVM.products.observeAsState(emptyList())
    var searchText by remember { mutableStateOf("") }
    var active by remember { mutableStateOf(false) }
    val searchProducts: List<Products> by productVM.searchProducts.observeAsState(emptyList())

    var isSearching by remember { mutableStateOf(false) }

    Scaffold(

        content = {

            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 40.dp, start = 40.dp, end = 20.dp, bottom = 20.dp)
                ) {

                    Text(
                        text = "Discover",
                        color = Color.Black,
                        style = Typography.h1,
                        modifier = Modifier.weight(1f)
                    )


                    IconButton(
                        onClick = {
                            navController.navigate(Routes.AddProduct.route)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add",
                            tint = Color.Black
                        )
                    }
                }

                SearchBar(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally),
                    query = searchText,
                    onQueryChange = {
                        searchText = it
                    },
                    onSearch = {
                        active = false

                        productVM.performSearch(searchText)

                        isSearching = true

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
                                        productVM.clearSearchResults()
                                    } else {
                                        active = false
                                        isSearching = false
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
                SwipeRefresh(
                    state = swipeRefreshState,
                    onRefresh = productVM::getProductData
                ) {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(start = 20.dp, end = 20.dp, bottom = 70.dp),
                    ) {
                        val displayedProducts = if (isSearching) searchProducts else products
                        if (isSearching && displayedProducts.isEmpty()) {
                            item {

                                Text(
                                    text = "No Result Found.",
                                    style = Typography.body2
                                )

                            }
                        } else {
                            items(displayedProducts) { product ->

                                if (isSearching && searchProducts.isEmpty()) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(200.dp)
                                            .align(Alignment.CenterHorizontally)
                                    ) {
                                        Text(
                                            text = "No Result Found.",
                                            modifier = Modifier.align(Alignment.Center),
                                            textAlign = TextAlign.Center,
                                            style = Typography.body2
                                        )
                                    }

                                } else {
                                    Card(
                                        onClick = {
                                            navController.navigate(
                                                route = Routes.ProductDetails.route + "/${product.productID}"
                                            )
                                        },
                                        modifier = Modifier
                                            .size(width = 180.dp, height = 200.dp)
                                            .align(Alignment.CenterHorizontally)
                                    ) {


                                        AsyncImage(
                                            model = product.imageURL,
                                            contentDescription = null,
                                            contentScale = ContentScale.FillBounds,
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
                    }
                }



            }


        },
        bottomBar = {
            CustomBottomBar(navController = navController)
        }
    )


}



