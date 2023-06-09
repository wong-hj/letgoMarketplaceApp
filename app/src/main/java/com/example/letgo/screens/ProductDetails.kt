package com.example.letgo.screens

import android.location.Geocoder
import android.util.Log
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontVariation.weight
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.letgo.R
import com.example.letgo.models.Offers
import com.example.letgo.models.Products
import com.example.letgo.models.Reviews
import com.example.letgo.models.Users
import com.example.letgo.ui.theme.Typography
import com.example.letgo.viewModel.EditProductViewModel
import com.example.letgo.viewModel.LikedViewModel
import com.example.letgo.viewModel.OfferViewModel
import com.example.letgo.viewModel.ProductDetailsViewModel
import com.example.letgo.widgets.*
import com.google.android.gms.maps.model.CameraPosition
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.type.LatLng


@Composable
fun ProductDetails(navController: NavHostController, vm: ProductDetailsViewModel = viewModel(), offerVM: OfferViewModel = viewModel()) {

    val arguments = navController.currentBackStackEntry?.arguments
    val documentId = arguments?.getString("productID")

    val user by vm.getUser.observeAsState()

    val currentUser by vm.getCurrentUser.observeAsState()


    val product by vm.getData.observeAsState()

    var buyerIdDefaultOffered  by remember { mutableStateOf(false) }
    var productIdDefaultOffered by remember { mutableStateOf(false) }
    val offerProducts: List<Offers> by offerVM.buyerOfferProducts.observeAsState(emptyList())
    val buyerIDsArray: Array<String> = offerProducts.map { it.buyerID }.toTypedArray()
    val productIDsArray: Array<String> = offerProducts.map { it.productID }.toTypedArray()

    buyerIdDefaultOffered = buyerIDsArray.contains(currentUser?.userID)
    productIdDefaultOffered = productIDsArray.contains(product?.productID)

    val offerProductID: Offers? = offerProducts.firstOrNull { it.productID == product?.productID }

    val userID = product?.userID

    vm.getData(documentId.toString())

    if (userID != null) {
        vm.getProductUser(userID)
    }
    vm.getCurrentUser()
    var productDefaultLiked  by remember { mutableStateOf(false) }

    productDefaultLiked = currentUser?.likedProducts?.contains(product?.productID) == true

    val addressMap = product?.location

    var likeCounter by remember { mutableStateOf(-1) }

    likeCounter = product?.likes ?: 0

    val context = LocalContext.current
    var isSpecificationExpanded by remember { mutableStateOf(true) }

    var isMeetupExpanded by remember { mutableStateOf(true) }

    var isDialogVisible by remember { mutableStateOf(false) }
    var isReviewDialogVisible by remember { mutableStateOf(false) }
    var enteredText by remember { mutableStateOf("") }

    enteredText = product?.price.toString()
    var reviewText by remember { mutableStateOf("") }
    var selectedRating by remember { mutableStateOf(0) }
    //get reviews
    vm.getReviews(product?.productID ?: "")
    val reviews: List<Reviews> by vm.reviewsListByProduct.observeAsState(emptyList())

    Column(modifier = Modifier
        .fillMaxWidth()
        .verticalScroll(rememberScrollState())) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            // Image
            AsyncImage(
                //model = "https://firebasestorage.googleapis.com/v0/b/letgo-b3706.appspot.com/o/images%2FLego%20Gun.jpg?alt=media&token=35edb901-233c-43be-86e1-aef54cedd304&_gl=1*1tb4zfb*_ga*MTM3MDUyNzAyOC4xNjg0NTg1OTU1*_ga_CW55HF8NVT*MTY4NjE0OTI4Ny4xMi4wLjE2ODYxNDkyODcuMC4wLjA.",
                model = product?.imageURL,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .clip(
                        RoundedCornerShape(
                            bottomEnd = 30.dp,
                            bottomStart = 30.dp
                        )
                    )
            )
            // Back button
            IconButton(
                onClick = { navController.navigateUp() },
                modifier = Modifier
                    .align(Alignment.TopStart)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.Black,
                    modifier = Modifier.size(30.dp)
                )
            }
        }

        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)) {
            // Title
            Text(
                //text = "Dog",
                text = product?.name ?: "",
                color = Color.Black,
                style = Typography.h2
            )
            //Likes

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Icon",
                    tint = Color.Black,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = if(likeCounter > 0) { "$likeCounter like this" } else { "0 like this" },
                    color = Color.DarkGray,
                    style = Typography.subtitle1,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }

            //Description Section
            CustomIconText(value = "Description", icon = Icons.Default.Subject)

            Text(
                //text = "DESC",
                text = product?.description ?: "",
                color = Color.DarkGray,
                style = Typography.subtitle2
            )

            Divider(
                color = Color.LightGray,
                thickness = 1.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )
            // Specification Section
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 10.dp, bottom = 10.dp)) {

                Row(
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Icon",
                        tint = Color.Black,
                        modifier = Modifier.size(24.dp)
                    )

                    Text(
                        text = "Specification",
                        color = Color.Black,
                        style = Typography.subtitle1,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }

                IconButton(
                    onClick = {
                        isSpecificationExpanded = !isSpecificationExpanded
                    }
                ) {
                    Icon(
                        imageVector = if (isSpecificationExpanded) Icons.Default.ExpandMore else Icons.Default.ExpandLess,
                        contentDescription = "Toggle Section",
                        tint = Color.Black,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            if (isSpecificationExpanded) {
                CustomSmallSection(header= "Brand", value = product?.brand ?: ""

                )

                Spacer(modifier = Modifier.height(10.dp))

                CustomSmallSection(header= "Quality", value = product?.quality ?: ""
                )
            }

            Divider(
                color = Color.LightGray,
                thickness = 1.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )

            //Meetup Section
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 10.dp, bottom = 10.dp)) {

                Row(
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Default.EmojiPeople,
                        contentDescription = "Icon",
                        tint = Color.Black,
                        modifier = Modifier.size(24.dp)
                    )

                    Text(
                        text = "Meetup",
                        color = Color.Black,
                        style = Typography.subtitle1,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }

                IconButton(
                    onClick = {
                        isMeetupExpanded = !isMeetupExpanded
                    }
                ) {
                    Icon(
                        imageVector = if (isMeetupExpanded) Icons.Default.ExpandMore else Icons.Default.ExpandLess,
                        contentDescription = "Toggle Section",
                        tint = Color.Black,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            if (isMeetupExpanded) {

                val geocoder = Geocoder(context)
                val results = addressMap?.let { geocoder.getFromLocationName(it, 1) }

                if (results != null) {

                    if (results.isNotEmpty()) {

                        val location = results[0]
                        val latitude = location.latitude
                        val longitude = location.longitude


                        val mapLocation =
                            com.google.android.gms.maps.model.LatLng(latitude, longitude)
                        val locationState = MarkerState(position = mapLocation)
                        val cameraPositionState = rememberCameraPositionState {
                            position = CameraPosition.fromLatLngZoom(mapLocation, 15f)
                        }

                        GoogleMap(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(450.dp),
                            cameraPositionState = cameraPositionState
                        ) {
                            Marker(
                                state = locationState,
                                title = "Location"
                            )
                        }
                    }
                }

            }

            Divider(
                color = Color.LightGray,
                thickness = 1.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )


            CustomIconText(value = "Seller", icon = Icons.Default.Storefront)

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(5.dp),
                    colors = CardDefaults.cardColors(
                    containerColor = Color.White,
                ),

            ) {
                Row(modifier = Modifier.padding(16.dp)) {
                    // Circle Image

                    Image(
                        painter = painterResource(id = R.drawable.user),
                        contentDescription = "User Image",
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                    )

                    // Content
                    Column(modifier = Modifier.padding(start = 16.dp)) {
                        Text(text = user?.name ?: "", style = Typography.subtitle1)
                        Text(text = user?.university ?: "", style = Typography.subtitle2)
                        Text(text = user?.studentID ?: "", style = Typography.subtitle2)
                    }
                }
            }

            Divider(
                color = Color.LightGray,
                thickness = 1.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp)
            )

            // Review Section
            CustomIconText(value = "Reviews", icon = Icons.Default.RateReview)

            if (reviews.isNotEmpty()) {
                reviews.forEach { item ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 25.dp, vertical = 5.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            androidx.compose.material.Text(
                                text = item.userName,
                                style = Typography.h3,
                                modifier = Modifier.padding(end = 10.dp)
                            )
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null
                            )
                            androidx.compose.material.Text(
                                text = item.rating.toString(),
                                style = Typography.subtitle1
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        androidx.compose.material.Text(
                            text = item.review,
                            style = Typography.subtitle1
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Divider()
                    }

                }
            } else {
                Text("There are currently no reviews for this product.", style = Typography.subtitle1)
            }

        }


        //Bottom Nav Section
        BottomAppBar(
            modifier = Modifier.fillMaxWidth().padding(top = 5.dp),
            containerColor = MaterialTheme.colorScheme.primaryContainer
            //backgroundColor = Color.White,
            //elevation = 8.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    // = "RM 699",
                    text = "RM ${product?.price.toString()}",
                    style = Typography.h2,
                    modifier = Modifier.weight(1f)
                )
                if (productIdDefaultOffered && buyerIdDefaultOffered) {

                    if ((offerProductID?.status ?: "") == "Accept") {

                        Button(
                            onClick = { isReviewDialogVisible = true },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary),
                            ) {
                            Text(text = "Give Review")
                        }

                    } else {
                        Button(
                            onClick = { /* Handle chat button click */ },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),

                            ) {
                            Text(text = "Offer Sent")
                        }
                    }




                } else {
                    if(currentUser?.userID != user?.userID) {

                        Button(
                            onClick = { isDialogVisible = true },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                        ) {
                            Text(text = "Make Offer")
                        }
                    }
                }



                if (isDialogVisible) {
                    AlertDialog(
                        onDismissRequest = { isDialogVisible = false },
                        title = { Text(text = "Make An Offer") },
                        text = {
                            OutlinedTextField(
                                value = enteredText,
                                onValueChange = { enteredText = it },
                                label = { Text(text = "Offer") },
                                modifier = Modifier.fillMaxWidth()
                            )
                        },
                        confirmButton = {
                            Button(
                                onClick = {
                                    // Handle confirm button click
                                    vm.makeOffer(product?.productID, product?.imageURL, enteredText, product?.name, user?.userID, currentUser?.name) { success ->
                                        if (success) {
                                            offerVM.getBuyerOfferData()
                                            isDialogVisible = false
                                        }

                                    }


                                    // Access the entered text via the 'enteredText' variable

                                }
                            ) {
                                Text(text = "Confirm")
                            }
                        },
                        dismissButton = {
                            Button(
                                onClick = { isDialogVisible = false },
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                            ) {
                                Text(text = "Cancel")
                            }
                        }
                    )
                }

                if (isReviewDialogVisible) {
                    AlertDialog(
                        onDismissRequest = { isReviewDialogVisible = false },
                        title = { Text(text = "Give a Review") },
                        text = {
                            Column {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(text = "Rate:")
                                    for (i in 1..5) {
                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            modifier = Modifier
                                                .clickable {
                                                    selectedRating = i
                                                    /* Handle rating selection */
                                                }
                                        ) {
                                            RadioButton(
                                                selected = selectedRating == i,
                                                onClick = { selectedRating = i },
                                                colors = RadioButtonDefaults.colors(
                                                    selectedColor = MaterialTheme.colorScheme.primary
                                                )
                                            )
                                            Text(
                                                text = i.toString()
                                            )
                                        }
                                    }
                                }

                                OutlinedTextField(
                                    value = reviewText,
                                    onValueChange = { reviewText = it },
                                    label = { Text(text = "Review") },
                                    modifier = Modifier.fillMaxWidth().height(100.dp)
                                )


                            }

                        },
                        confirmButton = {
                            Button(
                                onClick = {
                                    // Handle confirm button click
                                    vm.giveReview(selectedRating, reviewText, currentUser?.userID, currentUser?.name, user?.userID, offerProductID?.offerID, product?.productID) { success ->
                                        if (success) {
                                            offerVM.getBuyerOfferData()
                                            isReviewDialogVisible = false
                                        }

                                    }

                                }
                            ) {
                                Text(text = "Confirm")
                            }
                        },
                        dismissButton = {
                            Button(
                                onClick = { isReviewDialogVisible = false },
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                            ) {
                                Text(text = "Cancel")
                            }
                        }
                    )
                }



                IconButton(
                    onClick = {

                        if(productDefaultLiked) {
                            // Unlike
                            vm.unlikeProduct(product?.productID ?: "", product?.likes ?: 0) { success ->
                                if(success) {

                                    vm.getData(documentId.toString())
                                    likeCounter.minus(1)

                                }
                            }
                        }


                        if (!productDefaultLiked) {
                            // Like
                            vm.addLikeProduct(product?.productID ?: "") { success ->
                                if (success) {
                                    vm.getData(documentId.toString())
                                    likeCounter.plus(1)

                                }
                            }
                        }

                    }

                ) {

                    Icon(
                        imageVector = if (productDefaultLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Love Icon",
                        tint = Color.Red
                    )

                }
            }
        }

    }



}




