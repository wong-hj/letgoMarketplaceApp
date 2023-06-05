package com.example.letgo.screens

import android.graphics.Paint
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontVariation.width
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.letgo.models.Products
import com.example.letgo.nav.Routes
import com.example.letgo.rememberImeState
import com.example.letgo.ui.theme.Typography
import com.example.letgo.viewModel.AddProductViewModel
import com.example.letgo.viewModel.LikedViewModel
import com.example.letgo.viewModel.LoginViewModel
import com.example.letgo.widgets.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProduct(vm: AddProductViewModel = viewModel()) {

    val scope = rememberCoroutineScope()


    val name = remember { mutableStateOf("") }
    val description = remember { mutableStateOf("") }
    val brand = remember { mutableStateOf("") }
    val location = remember { mutableStateOf("") }
    val price = remember { mutableStateOf("") }

    //dropdown
    val qualityOptions = listOf("Brand New", "Like New", "Light Used", "Heavy Used")
    //val defaultQuality = "Quality"
    var selectedQuality by remember { mutableStateOf(qualityOptions[0]) }
    var expanded by remember { mutableStateOf(false) }
    //add back button here
//    TopAppBar(
//        title = { Text(text = "Be a Seller") },
//        navigationIcon = {
//            IconButton(onClick = { /* Handle back button click */ }) {
//                Icon(
//                    imageVector = Icons.Default.ArrowBack,
//                    contentDescription = "Back"
//                )
//            }
//        }
//    )
    val imeState = rememberImeState()
    val scrollState = rememberScrollState()


    LaunchedEffect(key1 = imeState.value) {
        if (imeState.value) {
            scrollState.scrollTo(scrollState.maxValue)
        }
    }

    var selectedImageUri by remember {
        mutableStateOf<Uri?>(null)
    }

    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri -> selectedImageUri = uri }
    )

    Column(modifier = Modifier
        .fillMaxWidth()
        .verticalScroll(scrollState)) {
        CustomHeader(value = "Be a Seller.")

        Spacer(modifier = Modifier.height(20.dp))

        CustomOutlinedTextField(
            value = name.value,
            onValueChangeFun = {name.value = it},
            labelText = "Product Name"
        )

        Spacer(modifier = Modifier.height(20.dp))

        CustomTextArea(
            value = description.value,
            onValueChangeFun = {description.value = it},
            labelText = "Description")

        Spacer(modifier = Modifier.height(20.dp))

        CustomOutlinedTextField(
            value = brand.value,
            onValueChangeFun = {brand.value = it},
            labelText = "Brand"
        )

        Spacer(modifier = Modifier.height(20.dp))

        ExposedDropdownMenuBox(
            modifier = Modifier
                .align(Alignment.CenterHorizontally),
            expanded = expanded,
            onExpandedChange = { expanded = it }
        ) {

            OutlinedTextField(
                value = selectedQuality,
                onValueChange = {},
                readOnly = true,
                label = { Text(text = "Quality", style = Typography.body2) },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                modifier = Modifier
                    .menuAnchor()

            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                qualityOptions.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(text = option) },
                        onClick = {
                            selectedQuality = option
                            expanded = false
                        }
                    )
                }
            }
        }


        Spacer(modifier = Modifier.height(20.dp))

        CustomOutlinedTextField(
            value = location.value,
            onValueChangeFun = {location.value = it},
            labelText = "Meetup Location"
        )

        Spacer(modifier = Modifier.height(20.dp))

        CustomOutlinedTextField(
            value = price.value,
            onValueChangeFun = {price.value = it},
            labelText = "Price"
        )

        Spacer(modifier = Modifier.height(20.dp))

        AsyncImage(
            model = selectedImageUri,
            contentDescription = null,
            modifier = Modifier.width(280.dp).height(300.dp).align(Alignment.CenterHorizontally),
            contentScale = ContentScale.Crop
        )

        Button(
            onClick = {
                singlePhotoPickerLauncher.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
            }
        ) {
            Text(text = "Pick photo")
        }


        var isLoading by remember { mutableStateOf(false) }

//        CustomButton(
//
//            btnText = "Create",
//            btnColor = MaterialTheme.colorScheme.tertiary,
//            onClickFun = {
//                scope.launch {
//                    isLoading = true
//                    val data = vm.addProduct()
//
//                    if(data!= null) {
//
//                        navController.navigate(Routes.HomePage.route)
//
//                    } else {
//
//                        showLoginError = true
//
//                    }
//                    isLoading = false
//                }
//            }
//
//        )
        Button(
            shape = RoundedCornerShape(5.dp),
            modifier = Modifier.width(280.dp).align(Alignment.CenterHorizontally),
            onClick = {

            },
        ) {
            Text(
                text = "Create",
                color = MaterialTheme.colorScheme.background
            )
        }

    }

}