package com.example.letgo.screens

import android.annotation.SuppressLint
import android.util.Patterns
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.letgo.rememberImeState
import com.example.letgo.ui.theme.Typography
import com.example.letgo.viewModel.EditUserViewModel
import com.example.letgo.widgets.CustomButton
import com.example.letgo.widgets.CustomOutlinedTextField
import kotlinx.coroutines.launch

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditUser(navController: NavHostController, editVM: EditUserViewModel = viewModel()) {


    val user by editVM.getUser.observeAsState()

    //For Edit
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var cfmPassword by remember { mutableStateOf("") }
    var fullName by remember { mutableStateOf("") }
    var university by remember { mutableStateOf("") }
    var studentID by remember { mutableStateOf("") }
    var contact by remember { mutableStateOf("") }

    var validateName by rememberSaveable { mutableStateOf(true) }
    var validateEmail by rememberSaveable { mutableStateOf(true) }
    var validateCfmPassword by rememberSaveable { mutableStateOf(true) }
    var validatePassword by rememberSaveable { mutableStateOf(true) }
    var validateUniversity by rememberSaveable { mutableStateOf(true) }
    var validateStudentID by rememberSaveable { mutableStateOf(true) }
    var validateContact by rememberSaveable { mutableStateOf(true) }

    fun validateData(): Boolean {

        validateEmail = Patterns.EMAIL_ADDRESS.matcher(email).matches()
        validateName = fullName.isNotBlank()
        validatePassword =  password == cfmPassword
        validateCfmPassword =  password == cfmPassword
        validateUniversity = university.isNotBlank()
        validateStudentID = studentID.isNotBlank()
        validateContact = contact.isNotBlank()

        return validateEmail && validateName && validatePassword && validateCfmPassword && validateUniversity && validateStudentID && validateContact
    }

    LaunchedEffect(Unit) {
        user?.let {
            email = it.email
            fullName = it.name
            university = it.university
            studentID = it.studentID
            contact = it.contact

        }
    }

    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val imeState = rememberImeState()
    val scrollState = rememberScrollState()

    LaunchedEffect(key1 = imeState.value) {
        if (imeState.value) {
            scrollState.scrollTo(scrollState.maxValue)
        }
    }


    Column(modifier = Modifier.fillMaxWidth()) {

        // Back button
        IconButton(
            onClick = { navController.navigateUp() },
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                tint = Color.Black,
                modifier = Modifier.size(30.dp)
            )
        }

        Column(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .padding(top = 10.dp, start = 30.dp, end = 30.dp, bottom = 20.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = "Edit Profile - \n$fullName",
                color = Color.Black,
                style = Typography.h1
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            //verticalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth().verticalScroll(scrollState)
        ) {
            CustomOutlinedTextField(
                value = email,
                onValueChangeFun = { email = it },
                labelText = "Email Address",
                showError = !validateEmail,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                leadingIconImageVector = Icons.Default.Email
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Password
            var isPasswordVisible by rememberSaveable { mutableStateOf(false) }

            CustomOutlinedTextField(
                value = password,
                onValueChangeFun = { password = it },
                labelText = "Password",
                showError = !validatePassword,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                isPasswordField = true,
                isPasswordVisible = isPasswordVisible,
                onVisibilityChange = { isPasswordVisible = it },
                leadingIconImageVector = Icons.Default.Lock,
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Confirm Password
            var isConfirmPasswordVisible by rememberSaveable {
                mutableStateOf(
                    false
                )
            }

            CustomOutlinedTextField(
                value = cfmPassword,
                onValueChangeFun = { cfmPassword = it },
                labelText = "Confirm Password",
                showError = !validateCfmPassword,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                isPasswordField = true,
                isPasswordVisible = isConfirmPasswordVisible,
                onVisibilityChange = { isConfirmPasswordVisible = it },
                leadingIconImageVector = Icons.Default.Lock,
            )

            Spacer(modifier = Modifier.height(10.dp))

            CustomOutlinedTextField(
                value = fullName,
                onValueChangeFun = { fullName = it },
                labelText = "Full Name",
                showError = !validateName,
                leadingIconImageVector = Icons.Default.Person
            )

            Spacer(modifier = Modifier.height(10.dp))

            CustomOutlinedTextField(
                value = university,
                onValueChangeFun = { university = it },
                labelText = "Tertiary Study",
                showError = !validateUniversity,
                leadingIconImageVector = Icons.Default.School
            )

            Spacer(modifier = Modifier.height(10.dp))

            CustomOutlinedTextField(
                value = studentID,
                onValueChangeFun = { studentID = it },
                labelText = "Student ID",
                showError = !validateStudentID,
                leadingIconImageVector = Icons.Default.Badge
            )

            Spacer(modifier = Modifier.height(10.dp))

            CustomOutlinedTextField(
                value = contact,
                onValueChangeFun = { contact = it },
                labelText = "Contact Number",
                showError = !validateContact,
                leadingIconImageVector = Icons.Default.Call
            )

            Spacer(modifier = Modifier.height(20.dp))
            var isLoading by remember { mutableStateOf(false) }

            CustomButton(
                btnText = "Update",
                btnColor = MaterialTheme.colorScheme.tertiary,
                onClickFun = {
                    scope.launch {
                        isLoading = true
                        if(validateData()) {
                            val data = editVM.updateUser(
                                email,
                                fullName,
                                university,
                                studentID,
                                contact,
                                password
                            )

                            if (data) {
                                navController.navigateUp()
                            }
                        }
                        isLoading = false
                    }
                }

            )
        }

    }
}


