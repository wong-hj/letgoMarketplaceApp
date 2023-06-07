package com.example.letgo.screens

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.letgo.rememberImeState
import com.example.letgo.ui.theme.Typography
import com.example.letgo.viewModel.EditProductViewModel
import com.example.letgo.viewModel.HomePageViewModel
import com.example.letgo.widgets.CustomButton
import com.example.letgo.widgets.CustomOutlinedTextField
import com.example.letgo.widgets.CustomTextArea
import kotlinx.coroutines.launch
import org.checkerframework.checker.units.qual.s


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProduct(navController: NavHostController, vm: EditProductViewModel = viewModel(), productVM: HomePageViewModel = viewModel()) {
    val arguments = navController.currentBackStackEntry?.arguments
    val documentId = arguments?.getString("productID")

    val product by vm.getProduct(documentId.toString()).observeAsState()
    val openDialog = remember { mutableStateOf(false)  }

    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val name = remember {  mutableStateOf("") }
    val description = remember { mutableStateOf("") }
    val brand = remember { mutableStateOf("") }
    val location = remember { mutableStateOf("") }
    val price = remember { mutableStateOf("") }

    //dropdown
    val qualityOptions = listOf("Brand New", "Like New", "Light Used", "Heavy Used")
    //val defaultQuality = "Quality"
    var selectedQuality by remember { mutableStateOf(product?.quality ?: qualityOptions[0]) }
    var expanded by remember { mutableStateOf(false) }
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
        IconButton(
            onClick = { navController.navigateUp() }
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                tint = Color.Black
            )
        }

        Row(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 10.dp, start = 40.dp, end = 20.dp, bottom = 20.dp)
        ) {

            Text(
                text = "Edit Listing",
                color = Color.Black,
                style = Typography.h1,
                modifier = Modifier.weight(1f)
            )


            IconButton(
                onClick = {
                    openDialog.value = true
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Add",
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(30.dp)
                )
            }
        }

        if (openDialog.value) {

            AlertDialog(
                onDismissRequest = {
                    // Dismiss the dialog when the user clicks outside the dialog or on the back
                    // button. If you want to disable that functionality, simply use an empty
                    // onCloseRequest.
                    openDialog.value = false
                },
                title = {
                    Text(text = "Delete Listing")
                },
                text = {
                    Text("Confirm Delete?")
                },
                confirmButton = {
                    Button(
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                        onClick = {


                            product?.let { vm.deleteProduct(it.productID, it.productID) }
                            openDialog.value = false
                            navController.navigateUp()

                        }) {
                        Text("Delete")
                    }
                },
                dismissButton = {
                    Button(

                        onClick = {
                            openDialog.value = false
                        }) {
                        Text("Cancel")
                    }
                }
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Box(
            modifier = Modifier
                .size(width = 280.dp, height = 250.dp)
                .border(
                    width = 1.dp, color = Color.LightGray,
                    shape = RoundedCornerShape(3.dp)
                )
                .align(Alignment.CenterHorizontally)
                .clickable {

                    singlePhotoPickerLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )

                }
        ) {

            AsyncImage(
                model = if(selectedImageUri != null) {selectedImageUri} else {product?.imageURL},
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(width = 280.dp, height = 250.dp)
            )

        }

        Spacer(modifier = Modifier.height(20.dp))


        CustomOutlinedTextField(
            value = name.value,
            onValueChangeFun = {name.value = it},
            labelText = product?.name ?: ""
        )


        Spacer(modifier = Modifier.height(20.dp))


        CustomTextArea(
            value = description.value,
            onValueChangeFun = {description.value = it},
            labelText = product?.description ?: ""
        )


        Spacer(modifier = Modifier.height(20.dp))


        CustomOutlinedTextField(
            value = brand.value,
            onValueChangeFun = {brand.value = it},
            labelText = product?.brand ?: ""
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
                onValueChange = { selectedQuality = it },
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
            labelText = product?.location ?: ""
        )


        Spacer(modifier = Modifier.height(20.dp))


        CustomOutlinedTextField(
            value = price.value,
            onValueChangeFun = {price.value = it},
            labelText = product?.price.toString()
        )


        Spacer(modifier = Modifier.height(20.dp))

        var isLoading by remember { mutableStateOf(false) }
        Box(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
        ) {
            CustomButton(

                btnText = "Update",
                btnColor = MaterialTheme.colorScheme.tertiary,
                onClickFun = {

                    val updateName = name.value.ifBlank {
                        product?.name ?: ""
                    }
                    val updateDesc = description.value.ifBlank {
                        product?.description ?: ""
                    }
                    val updateBrand = brand.value.ifBlank {
                        product?.brand ?: ""
                    }
                    val updateLocation = location.value.ifBlank {
                        product?.location ?: ""
                    }
                    val updatePrice = price.value.ifBlank {
                        product?.price.toString()
                    }
                    scope.launch {


                            vm.updateProduct(
                                selectedImageUri,
                                updateName,
                                updateDesc,
                                updateBrand,
                                selectedQuality,
                                updateLocation,
                                updatePrice,
                                product?.productID!!
                            )

                            isLoading = true

                            }

                            navController.navigateUp()

                        isLoading = false

                }

            )
        }

        Spacer(modifier = Modifier.height(20.dp))

    }
}