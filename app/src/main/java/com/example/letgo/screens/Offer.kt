package com.example.letgo.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.letgo.R
import com.example.letgo.models.Products
import com.example.letgo.models.Users
import com.example.letgo.nav.Routes
import com.example.letgo.ui.theme.Typography
import com.example.letgo.viewModel.LikedViewModel
import com.example.letgo.viewModel.OfferViewModel
import com.example.letgo.widgets.CustomBottomBar
import com.example.letgo.widgets.CustomHeader

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun Offer(
    //navController: NavHostController,
    vm: OfferViewModel = viewModel()
) {
    //val offerProducts: List<Users.Offer> by vm.offerProducts.observeAsState(emptyList())

    val offerProducts: List<Users> by vm.offerProducts.observeAsState(emptyList())
    Scaffold(

        content = {
            Column(modifier = Modifier.fillMaxWidth()) {

                CustomHeader(value = "Offers")

//                val products = listOf(
//                    Product("1", "Product 1", "$10.99", R.drawable.product1),
//                    Product("2", "Product 2", "$19.99", R.drawable.product2),
//                    Product("3", "Product 3", "$14.99", R.drawable.product3)
//                )

                val products = listOf(R.drawable.user, "Buzz", "RM 699")

                LazyColumn {
                    items(offerProducts) { offer ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                        ) {
                            Row(
                                modifier = Modifier
                                    .padding(16.dp)
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.user),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(80.dp)
                                        .clip(shape = RoundedCornerShape(8.dp))
                                )

                                Spacer(modifier = Modifier.width(16.dp))

                                Column {
                                    Text(
                                        text = "Make Offer",
                                        style = Typography.h3
                                    )
                                    Text(
                                        text = offerProducts.offerName,
                                        style = Typography.subtitle1
                                    )
                                    Text(
                                        text = "RM 599",
                                        style = Typography.subtitle2
                                    )
                                }

                                Spacer(modifier = Modifier.weight(1f))

                                Row {

                                    IconButton(
                                        onClick = { /* Handle tick button click */ },
                                        modifier = Modifier.padding(end = 8.dp)
                                    ) {
                                        Icon(
                                            Icons.Default.Check,
                                            contentDescription = "Tick Icon",
                                            tint = Color.Green,
                                            modifier = Modifier.size(30.dp)
                                        )
                                    }

                                    IconButton(
                                        onClick = { /* Handle cross button click */ }
                                    ) {
                                        Icon(
                                            Icons.Default.Close,
                                            contentDescription = "Cross Icon",
                                            tint = Color.Red,
                                            modifier = Modifier.size(30.dp)
                                        )
                                    }

                                }
                            }
                        }
//                    }
//                }

                Spacer(modifier = Modifier.height(20.dp))
            }
        }
//        },
//        bottomBar = {
//            CustomBottomBar(navController = navController)
//        }

    )
}