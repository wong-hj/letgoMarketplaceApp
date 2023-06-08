package com.example.letgo.screens

import android.location.Geocoder
import android.util.Log
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
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
import com.example.letgo.models.Products
import com.example.letgo.models.Users
import com.example.letgo.ui.theme.Typography
import com.example.letgo.viewModel.EditProductViewModel
import com.example.letgo.viewModel.LikedViewModel
import com.example.letgo.viewModel.ProductDetailsViewModel
import com.example.letgo.widgets.*
import com.google.android.gms.maps.model.CameraPosition
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.type.LatLng


@Composable
fun ProductDetails(navController: NavHostController, vm: ProductDetailsViewModel = viewModel(), editVM: EditProductViewModel = viewModel()) {

    val arguments = navController.currentBackStackEntry?.arguments
    val documentId = arguments?.getString("productID")
    Log.d("USERID123", documentId ?: "")
    val product by vm.getProduct(documentId.toString()).observeAsState()
    Log.d("USERIDNAME", product?.name ?: "")
    Log.d("USERID", product?.location ?: "")

    val userID = product?.userID

    Log.d("USERIDNAME", userID ?: "NOTIHNG")

    if (userID != null) {
        vm.getProductUser(userID)
    }

    val user by vm.getUser.observeAsState()
    val addressMap = product?.location


    //val user by vm.getUser("JWXznzANHwhimGZIfUf5GvjXZX22").observeAsState()
//    LaunchedEffect(product) {
//        val userId = product?.userID ?: ""
//        val userData = vm.getUser(userId).value
//        // Use the userData as needed
//        Log.d("USERID", userData?.name ?: "")
//    }


    val context = LocalContext.current
    var isSpecificationExpanded by remember { mutableStateOf(true) }

    var isMeetupExpanded by remember { mutableStateOf(true) }
    //Log.d("USERID2", product?.location ?: "")

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
            //Log.d("USERID3", product?.location ?: "")
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

        //Log.d("USERID4", product?.location ?: "")
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
            //Log.d("USERID5", product?.location ?: "")
            //Likes
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Icon",
                    tint = Color.Black,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = "${
                        product?.likes.toString()
                    } likes this",
                    color = Color.DarkGray,
                    style = Typography.subtitle1,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
            //Log.d("USERID6", product?.location ?: "")
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
            //Log.d("USERID7", product?.location ?: "")
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
                CustomSmallSection(header= "Brand", value =
                product?.brand ?: ""
                //"Brand"
                )

                Spacer(modifier = Modifier.height(10.dp))

                CustomSmallSection(header= "Quality", value =
                    //"quality"
                product?.quality ?: ""
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
        }

        BottomAppBar(
            modifier = Modifier.fillMaxWidth(),
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
                    text = product?.price.toString(),
                    style = Typography.h2,
                    modifier = Modifier.weight(1f)
                )

                Button(
                    onClick = { /* Handle chat button click */ },
                    //modifier = Modifier.padding(end = 8.dp)
                ) {
                    Text(text = "Chat")
                }

                IconButton(
                    onClick = { /* Handle love icon click */ }
                ) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = "Love Icon",
                        tint = Color.Red
                    )
                }
            }
        }

    }

}