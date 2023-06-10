package com.example.letgo.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.letgo.models.Products
import com.example.letgo.nav.Routes
import com.example.letgo.ui.theme.Typography
import com.example.letgo.widgets.CustomBottomBar
import com.example.letgo.widgets.CustomHeader
import com.example.letgo.viewModel.LikedViewModel

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun Liked(navController: NavHostController, likeVM: LikedViewModel = viewModel()) {

    val products: List<Products> by likeVM.likedProducts.observeAsState(emptyList())

    Scaffold(

        content = {

            Column(modifier = Modifier.fillMaxWidth()) {
                CustomHeader(value = "Liked.")

                Spacer(modifier = Modifier.height(20.dp))
                if (products.isNotEmpty()) {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier
                            .padding(start = 20.dp, end = 20.dp, bottom = 70.dp)
                    ) {
                        items(products) { product ->
                            Card(
                                onClick = {
                                    navController.navigate(
                                        route = Routes.ProductDetails.route + "/${product.productID}"
                                    )
                                },
                                modifier = Modifier
                                    .size(width = 180.dp, height = 200.dp)
                            ) {

                                AsyncImage(
                                    model = product.imageURL,
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop,
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
                } else {
                    Text(
                        "There are currently no reviews.",
                        style = Typography.subtitle1
                    )
                }
            }
        },
        bottomBar = {
            CustomBottomBar(navController = navController)
        }
    )
}