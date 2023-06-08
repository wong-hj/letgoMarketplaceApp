package com.example.letgo.screens

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.letgo.R
import com.example.letgo.nav.Routes
import com.example.letgo.ui.theme.Typography
import com.example.letgo.viewModel.UserViewModel
import com.example.letgo.widgets.CustomBottomBar
import com.example.letgo.widgets.CustomHeader
import com.google.android.play.integrity.internal.y
import com.google.firebase.auth.FirebaseAuth
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import coil.compose.AsyncImage
import com.example.letgo.models.Products
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun Profile(navController : NavHostController, userVM: UserViewModel = viewModel()) {
    val isLoading by userVM.isLoading.collectAsState()
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = isLoading)

    val user = userVM.state.value
    val auth = FirebaseAuth.getInstance()
    val userListings: List<Products> by userVM.userListings.observeAsState(emptyList())

    val tabTitles = listOf("Listings", "Reviews")
    val selectedTabIndex = remember { mutableStateOf(0) }

    Scaffold(

        content = {
            Column(modifier = Modifier.fillMaxWidth(),  horizontalAlignment = Alignment.CenterHorizontally) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                   // verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier.weight(1f)
                    ) {
                        CustomHeader(value = "Profile")
                    }

                    Icon(
                        imageVector = Icons.Default.Logout,
                        contentDescription = "Logout",
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .padding(end = 25.dp)
                            .offset(y = 10.dp)
                            .clickable {
                                auth.signOut()
                                navController.navigate(Routes.Login.route)
                            }
                    )
                }

                Image(
                    painter = painterResource(id = R.drawable.user),
                    contentDescription = "User Image",
                    modifier = Modifier
                        .size(150.dp)
                        .clip(CircleShape)
                )

                Spacer(modifier = Modifier.height(15.dp))

                Text(text = user.name, style = Typography.h3)
                Text(text = user.university, style = Typography.subtitle1)

                Spacer(modifier = Modifier.height(15.dp))

                TabRow(
                    selectedTabIndex = selectedTabIndex.value,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    tabTitles.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTabIndex.value == index,
                            onClick = { selectedTabIndex.value = index },
                            text = { Text(title) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                // Content based on the selected tab
                when (selectedTabIndex.value) {

                    0 -> {
                        userVM.performFetchUserListing()
                        // Show Listings content
                        SwipeRefresh(
                            state = swipeRefreshState,
                            onRefresh = userVM::getServiceData
                        ) {
                            LazyVerticalGrid(
                                columns = GridCells.Fixed(2),
                                verticalArrangement = Arrangement.spacedBy(12.dp),
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                modifier = Modifier
                                    .padding(
                                        start = 20.dp,
                                        end = 20.dp,
                                        bottom = 70.dp,
                                        top = 20.dp
                                    ),
                            ) {

                                items(userListings) { product ->
                                    Card(
                                        onClick = {
                                            navController.navigate(
                                                route = Routes.EditProduct.route + "/${product.productID}"
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
                                            androidx.compose.material3.Text(
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
                    1 -> {
                        // Show Reviews content
                        Text(text = "Testing")
                    }
                }
            }
        },
        bottomBar = {
            CustomBottomBar(navController = navController)
        }
    )
}