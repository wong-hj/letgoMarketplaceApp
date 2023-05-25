package com.example.letgo.screens

import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.letgo.R
import com.example.letgo.nav.Routes
import com.example.letgo.rememberImeState
import com.example.letgo.viewModel.RegisterViewModel
import com.example.letgo.widgets.CustomButton
import com.example.letgo.widgets.CustomHeader
import com.example.letgo.widgets.CustomOutlinedTextField
import kotlinx.coroutines.launch

@Composable
fun Register(navController : NavHostController, vm: RegisterViewModel = viewModel()) {

    var email = remember { mutableStateOf("") }
    var password = remember { mutableStateOf("") }
    var cfmPassword = remember { mutableStateOf("") }

    var fullName = remember { mutableStateOf("") }
    var university = remember { mutableStateOf("") }
    var studentID = remember { mutableStateOf("") }

    var validateName by rememberSaveable { mutableStateOf(true) }
    var validateEmail by rememberSaveable { mutableStateOf(true) }
    var validateCfmPassword by rememberSaveable { mutableStateOf(true) }
    var validatePassword by rememberSaveable { mutableStateOf(true) }
    var validateUniversity by rememberSaveable { mutableStateOf(true) }
    var validateStudentID by rememberSaveable { mutableStateOf(true) }

    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val imeState = rememberImeState()
    val scrollState = rememberScrollState()

    LaunchedEffect(key1 = imeState.value) {
        if (imeState.value) {
            scrollState.scrollTo(scrollState.maxValue)
        }
    }

    fun validateData(): Boolean {

        validateEmail = Patterns.EMAIL_ADDRESS.matcher(email.value).matches()
        validateName = fullName.value.isNotBlank()
        validatePassword = password.value.isNotBlank() && password.value == cfmPassword.value
        validateCfmPassword = cfmPassword.value.isNotBlank() && password.value == cfmPassword.value
        validateUniversity = university.value.isNotBlank()
        validateStudentID = studentID.value.isNotBlank()

        return validateEmail && validateName && validatePassword && validateCfmPassword && validateUniversity && validateStudentID
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        //verticalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier.fillMaxSize().verticalScroll(scrollState)
    ) {


        CustomHeader(
            value = "Register"
        )


        CustomOutlinedTextField(
            value = email.value,
            onValueChangeFun = {email.value = it},
            labelText = "Email Address",
            showError = !validateEmail,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            leadingIconImageVector = Icons.Default.Email
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Password
        var isPasswordVisible by rememberSaveable { mutableStateOf(false) }

        CustomOutlinedTextField(
            value = password.value,
            onValueChangeFun = { password.value = it },
            labelText = "Password",
            showError = !validatePassword,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            isPasswordField = true,
            isPasswordVisible = isPasswordVisible,
            onVisibilityChange = { isPasswordVisible = it},
            leadingIconImageVector = Icons.Default.Lock,
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Confirm Password
        var isConfirmPasswordVisible by rememberSaveable { mutableStateOf(false) }

        CustomOutlinedTextField(
            value = cfmPassword.value,
            onValueChangeFun = { cfmPassword.value = it },
            labelText = "Confirm Password",
            showError = !validateCfmPassword,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            isPasswordField = true,
            isPasswordVisible = isConfirmPasswordVisible,
            onVisibilityChange = { isConfirmPasswordVisible = it},
            leadingIconImageVector = Icons.Default.Lock,
        )

        Spacer(modifier = Modifier.height(10.dp))

        CustomOutlinedTextField(
            value = fullName.value,
            onValueChangeFun = {fullName.value = it},
            labelText = "Full Name",
            showError = !validateName,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            leadingIconImageVector = Icons.Default.Person
        )

        Spacer(modifier = Modifier.height(10.dp))

        CustomOutlinedTextField(
            value = university.value,
            onValueChangeFun = {university.value = it},
            labelText = "Tertiary Study",
            showError = !validateUniversity,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            leadingIconImageVector = Icons.Default.School
        )

        Spacer(modifier = Modifier.height(10.dp))

        CustomOutlinedTextField(
            value = studentID.value,
            onValueChangeFun = {studentID.value = it},
            labelText = "Student ID",
            showError = !validateStudentID,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            leadingIconImageVector = Icons.Default.Badge
        )

        Spacer(modifier = Modifier.height(50.dp))

        var isLoading by remember { mutableStateOf(false) }

        CustomButton(
            btnText = "Sign Up",
            btnColor = MaterialTheme.colorScheme.tertiary,
            onClickFun = {
                scope.launch {
                    isLoading = true
                    if(validateData()) {
                        var data = vm.signUp(email.value, password.value, fullName.value, university.value, studentID.value, context)

                        if(data) {

                            navController.navigate(Routes.Login.route)
                            Toast.makeText(context, "Successfully Registered, \nWelcome!", Toast.LENGTH_SHORT).show()

                        } else {
                            Toast.makeText(context, "Please review the fields", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(context, "Something is wrong.", Toast.LENGTH_SHORT).show()
                    }

                    isLoading = false
                }
            }

        )


        Spacer(modifier = Modifier.height(10.dp))

        // LoginLink
        Row {
            Text(
                text = "Have an Account? ",
                style = TextStyle(
                    fontSize = 14.sp,
                    color = Color.Black
                )
            )

            ClickableText(
                text = AnnotatedString("Login here."),
                onClick = {
                    navController.navigate(Routes.Login.route)
                },
                style = TextStyle(
                    fontSize = 14.sp,
                    textDecoration = TextDecoration.Underline,
                    color = MaterialTheme.colorScheme.primary
                )
            )
        }

    }
}