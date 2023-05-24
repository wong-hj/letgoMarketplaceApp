package com.example.letgo.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.letgo.R
import com.example.letgo.nav.Routes
import com.example.letgo.widgets.CustomButton
import com.example.letgo.widgets.CustomHeader
import com.example.letgo.widgets.CustomOutlinedTextField

@Composable
fun Register(navController : NavHostController) {

    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val cfmPassword = remember { mutableStateOf("") }

    val fullName = remember { mutableStateOf("") }
    val university = remember { mutableStateOf("") }
    val studentID = remember { mutableStateOf("") }

    val scope = rememberCoroutineScope()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        //verticalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier.fillMaxSize()
    ) {

        Column(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .padding(40.dp)
                .fillMaxWidth()
                .align(Alignment.Start)
        ) {
            CustomHeader(
                value = "Register"
            )
        }

        CustomOutlinedTextField(
            value = email.value,
            onValueChangeFun = {email.value = it},
            labelText = "Email Address",
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
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            leadingIconImageVector = Icons.Default.Person
        )

        Spacer(modifier = Modifier.height(10.dp))

        CustomOutlinedTextField(
            value = university.value,
            onValueChangeFun = {university.value = it},
            labelText = "Tertiary Study",
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            leadingIconImageVector = Icons.Default.School
        )

        Spacer(modifier = Modifier.height(10.dp))

        CustomOutlinedTextField(
            value = studentID.value,
            onValueChangeFun = {studentID.value = it},
            labelText = "Student ID",
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            leadingIconImageVector = Icons.Default.Badge
        )

        Spacer(modifier = Modifier.height(50.dp))

        CustomButton(
            btnText = "Sign Up",
            btnColor = MaterialTheme.colorScheme.tertiary,
            onClickFun = {
//                scope.launch {
//                    isLoading = true
//                    val data = vm.logInWithEmail()
//
//                    if(data!= null) {
//                        navController.navigate(Routes.HomePage.route)
//                    } else {
//                        showLoginError = true
//                    }
//                    isLoading = false
//                }
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