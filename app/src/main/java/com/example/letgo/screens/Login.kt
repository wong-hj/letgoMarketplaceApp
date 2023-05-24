package com.example.letgo.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.foundation.layout.ColumnScopeInstance.align
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AlternateEmail
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Password
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.AutofillType
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
import com.example.letgo.R
import com.example.letgo.ui.theme.Typography
import com.example.letgo.widgets.CustomButton
import com.example.letgo.widgets.CustomDialogClose
import com.example.letgo.widgets.CustomHeader
import com.example.letgo.widgets.CustomOutlinedTextField
import kotlinx.coroutines.launch


@Composable
fun Login(){

    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }

    val scope = rememberCoroutineScope()



    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        //verticalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier.fillMaxSize()
    ) {

        Spacer(modifier = Modifier.height(20.dp))

        //letGoLogo
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "LetGo_Logo",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(200.dp)
                .clip(RoundedCornerShape(100.dp))
        )
        Column(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .padding(40.dp)
                .fillMaxWidth()
                .align(Alignment.Start)
        ) {
            CustomHeader(
                value = "Login"
            )
        }

        CustomOutlinedTextField(
            value = email.value,
            onValueChangeFun = {email.value = it},
            labelText = "Email Address",
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            leadingIconImageVector = Icons.Default.Email
        )

        Spacer(modifier = Modifier.height(20.dp))

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

        Spacer(modifier = Modifier.height(15.dp))

        // LoginButton
        var showLoginError by remember { mutableStateOf(false) }
        var isLoading by remember { mutableStateOf(false) }


        CustomButton(
            btnText = "Login",
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


        Spacer(modifier = Modifier.height(20.dp))

        // RegisterLink
        Row {
            Text(
                text = "Don't Have an account yet? ",
                style = TextStyle(
                    fontSize = 14.sp,
                    color = Color.Black
                )
            )

            ClickableText(
                text = AnnotatedString("Register Here."),
                //modifier = Modifier.padding(20.dp),
                onClick = {
                    //navController.navigate(Routes.UserRegistration.route)
                },
                style = TextStyle(
                    fontSize = 14.sp,
                    textDecoration = TextDecoration.Underline,
                    color = MaterialTheme.colorScheme.primary
                )
            )
        }


        if (showLoginError) {
            CustomDialogClose(
                alertTitle = "Error",
                alertBody = "Error",
                onDismissFun = { showLoginError = false },
                btnCloseClick = { showLoginError = false }
            )
        }
    }
}