package com.example.letgo.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.letgo.models.Offers
import com.example.letgo.ui.theme.Typography
import com.example.letgo.viewModel.OfferViewModel
import com.example.letgo.widgets.CustomBottomBar
import com.example.letgo.widgets.CustomHeader

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun Offer(
    navController: NavHostController,
    vm: OfferViewModel = viewModel()
) {

    val offerProducts: List<Offers> by vm.offerProducts.observeAsState(emptyList())
    val offerBuyerProducts: List<Offers> by vm.buyerOfferProducts.observeAsState(emptyList())
    val tabTitles = listOf("Offers From", "Offers To")
    val selectedTabIndex = remember { mutableStateOf(0) }

    Scaffold(

        content = {
            Column(modifier = Modifier.fillMaxWidth()) {

                CustomHeader(value = "Offers")

                TabRow(
                    selectedTabIndex = selectedTabIndex.value,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    tabTitles.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTabIndex.value == index,
                            onClick = { selectedTabIndex.value = index },
                            text = { androidx.compose.material.Text(title) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                // Content based on the selected tab
                when (selectedTabIndex.value) {
                    //Offer From
                    0 -> {
                        if (offerProducts.isNotEmpty()) {

                            LazyColumn {
                                items(offerProducts) { offer ->
                                    if(offer.status != "Accept") {
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

                                                AsyncImage(
                                                    model = offer.imageURL,
                                                    contentDescription = null,
                                                    contentScale = ContentScale.Crop,
                                                    modifier = Modifier
                                                        .size(80.dp)
                                                        .clip(shape = RoundedCornerShape(8.dp))

                                                )

                                                Spacer(modifier = Modifier.width(16.dp))

                                                Column {
                                                    Text(
                                                        text = "Offer - RM ${offer.offerPrice}",
                                                        style = Typography.h3
                                                    )
                                                    Text(
                                                        text = offer.productName,
                                                        style = Typography.subtitle1
                                                    )
                                                    Text(
                                                        text = "From ${offer.buyerName}",
                                                        style = Typography.subtitle2
                                                    )
                                                }

                                                Spacer(modifier = Modifier.weight(1f))

                                                Row {

                                                    IconButton(
                                                        onClick = {
                                                            vm.acceptOffer(offer.offerID)
                                                            vm.getOfferData()
                                                        },
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
                                                        onClick = {
                                                            vm.denyOffer(offer.offerID)
                                                            vm.getOfferData()
                                                        }
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
                                    }
                                }
                            }
                        } else {
                            Column(
                                horizontalAlignment = Alignment.Start,
                                modifier = Modifier
                                    .padding(top = 40.dp, start = 40.dp, bottom = 20.dp)
                                    .fillMaxWidth()
                            ) {
                                Text(text = "No Offers or Pending Offers Made At the Moment.", style = Typography.h3)
                            }
                        }
                    }
                    //Offer To
                    1 -> {
                        if (offerBuyerProducts.isNotEmpty()) {

                            LazyColumn {
                                items(offerBuyerProducts) { offer ->
                                    Card(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp),
                                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer)
                                    ) {
                                        Row(
                                            modifier = Modifier
                                                .padding(16.dp)
                                                .fillMaxWidth(),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {

                                            AsyncImage(
                                                model = offer.imageURL,
                                                contentDescription = null,
                                                contentScale = ContentScale.Crop,
                                                modifier = Modifier
                                                    .size(80.dp)
                                                    .clip(shape = RoundedCornerShape(8.dp))

                                            )

                                            Spacer(modifier = Modifier.width(16.dp))

                                            Column {
                                                Text(
                                                    text = "Offer - RM ${offer.offerPrice}",
                                                    style = Typography.h3
                                                )
                                                Text(
                                                    text = offer.productName,
                                                    style = Typography.subtitle1
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        } else {
                            Column(
                                horizontalAlignment = Alignment.Start,
                                modifier = Modifier
                                    .padding(top = 40.dp, start = 40.dp, bottom = 20.dp)
                                    .fillMaxWidth()
                            ) {
                                Text(text = "No Offers Made At the Moment.", style = Typography.h3)
                            }
                        }
                    }
                }





                Spacer(modifier = Modifier.height(20.dp))
            }
        },
        bottomBar = {
            CustomBottomBar(navController = navController)
        }

    )
}