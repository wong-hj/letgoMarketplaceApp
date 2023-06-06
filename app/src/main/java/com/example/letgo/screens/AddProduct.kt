package com.example.letgo.screens

import android.graphics.Paint
import android.hardware.camera2.params.BlackLevelPattern
import android.net.Uri
import android.util.Patterns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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


    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var brand by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }

    //validation
    var validateName by rememberSaveable { mutableStateOf(true) }
    var validateDesc by rememberSaveable { mutableStateOf(true) }
    var validateBrand by rememberSaveable { mutableStateOf(true) }
    var validateLocation by rememberSaveable { mutableStateOf(true) }
    var validatePrice by rememberSaveable { mutableStateOf(true) }
    var validatePriceInt: Int?
    var validateImage by rememberSaveable { mutableStateOf(true) }

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

    fun validateData(): Boolean {

        validateName = name.isNotBlank()
        validateDesc = description.isNotBlank()
        validateBrand = brand.isNotBlank()
        validateLocation = location.isNotBlank()
        validatePrice = price.isNotBlank()
        validatePriceInt = price.toIntOrNull()
        validateImage = selectedImageUri != null

        return validateName && validateDesc && validateBrand && validateLocation && validatePrice && validateImage && validatePriceInt != null
    }

    Column(modifier = Modifier
        .fillMaxWidth()
        .verticalScroll(scrollState)) {
        CustomHeader(value = "Be a Seller.")

        Spacer(modifier = Modifier.height(20.dp))

        Box(
            modifier = Modifier
                .size(width = 280.dp, height = 250.dp)
                .border(width = 2.dp, color = Color.LightGray)
                .align(Alignment.CenterHorizontally)
                .clickable {

                    singlePhotoPickerLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )

                }
        ) {
            if (selectedImageUri != null) {
                AsyncImage(
                    model = selectedImageUri!!,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(width = 280.dp, height = 250.dp)
                )
            } else {
                Icon(
                    imageVector = Icons.Default.Image,
                    contentDescription = "Clickable Image",
                    tint = Color.DarkGray,
                    modifier = Modifier.size(32.dp).align(Alignment.Center)
                )


                Text(
                    text = "Click to Insert Picture",
                    style = Typography.body2,
                    modifier = Modifier.align(Alignment.Center).padding(top = 60.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        CustomOutlinedTextField(
            value = name,
            onValueChangeFun = {name = it},
            labelText = "Product Name"
        )

        Spacer(modifier = Modifier.height(20.dp))

        CustomTextArea(
            value = description,
            onValueChangeFun = {description = it},
            labelText = "Description")

        Spacer(modifier = Modifier.height(20.dp))

        CustomOutlinedTextField(
            value = brand,
            onValueChangeFun = {brand = it},
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
            value = location,
            onValueChangeFun = {location = it},
            labelText = "Meetup Location"
        )

        Spacer(modifier = Modifier.height(20.dp))

        CustomOutlinedTextField(
            value = price,
            onValueChangeFun = {price = it},
            labelText = "Price"
        )

        Spacer(modifier = Modifier.height(20.dp))

        var isLoading by remember { mutableStateOf(false) }
        Box(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
        ) {
            CustomButton(

                btnText = "Create",
                btnColor = MaterialTheme.colorScheme.tertiary,
                onClickFun = {
                scope.launch {
                    if(validateData()) {
                        isLoading = true

                        vm.addProduct(
                            selectedImageUri!!,
                            name,
                            description,
                            brand,
                            selectedQuality,
                            location,
                            price
                        )

                        isLoading = false
                    }
                }
                }

            )
        }

        Spacer(modifier = Modifier.height(20.dp))

    }

}