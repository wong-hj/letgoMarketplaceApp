package com.example.letgo.screens

import android.net.Uri
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
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.example.letgo.viewModel.AddProductViewModel
import com.example.letgo.widgets.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProduct(navController: NavHostController, vm: AddProductViewModel = viewModel()) {

    val scope = rememberCoroutineScope()
    val context = LocalContext.current

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
    val imeState = rememberImeState()
    val scrollState = rememberScrollState()


    LaunchedEffect(key1 = imeState.value) {
        if (imeState.value) {
            scrollState.scrollTo(scrollState.maxValue)
        }
    }

    //Select Image
    var selectedImageUri by remember {
        mutableStateOf<Uri?>(null)
    }

    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri -> selectedImageUri = uri }
    )

    //Validate Field
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
        IconButton(
            onClick = { navController.navigateUp() }
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                tint = Color.Black
            )
        }
        Column(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .padding(top = 10.dp, start = 40.dp, bottom = 20.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = "Become a Seller!",
                color = Color.Black,
                style = Typography.h1,
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Box(
            modifier = Modifier
                .size(width = 280.dp, height = 250.dp)
                .border(width = 1.dp, color = if(validateImage) { Color.LightGray } else { MaterialTheme.colorScheme.error }, shape = RoundedCornerShape(3.dp))
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
            labelText = "Product Name",
            showError = !validateName,
        )

        Spacer(modifier = Modifier.height(20.dp))

        CustomTextArea(
            value = description,
            onValueChangeFun = {description = it},
            labelText = "Description",
            showError = !validateDesc
        )

        Spacer(modifier = Modifier.height(20.dp))

        CustomOutlinedTextField(
            value = brand,
            onValueChangeFun = {brand = it},
            labelText = "Brand",
            showError = !validateBrand,
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
            labelText = "Meetup Location",
            showError = !validateLocation,
        )

        Spacer(modifier = Modifier.height(20.dp))

        CustomOutlinedTextField(
            value = price,
            onValueChangeFun = {price = it},
            labelText = "Price",
            showError = !validatePrice,
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

                            navController.navigateUp()


                        } else {
                            Toast.makeText(context, "Please Fill in all valid Fields.", Toast.LENGTH_SHORT).show()
                        }

                        isLoading = false
                    }
                }

            )
        }

        Spacer(modifier = Modifier.height(20.dp))

    }

}