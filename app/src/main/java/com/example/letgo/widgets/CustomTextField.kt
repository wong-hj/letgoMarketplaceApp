package com.example.letgo.widgets

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.letgo.ui.theme.Typography

@Composable
fun CustomOutlinedTextField(
    value: String,
    onValueChangeFun: (String) -> Unit,
    labelText: String,
    isPasswordField: Boolean = false,
    isPasswordVisible: Boolean = false,
    onVisibilityChange: (Boolean) -> Unit = {},
    leadingIconImageVector: ImageVector? = null,
    leadingIconDescription: String = "",
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    showError: Boolean = false,
    errorMessage: String = ""
) {

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val leadingIcon: (@Composable () -> Unit)? = if (leadingIconImageVector != null) {
            {
                Icon(
                    imageVector = leadingIconImageVector,
                    contentDescription = leadingIconDescription
                )
            }
        } else {
            null
        }
        OutlinedTextField(
            value = value,
            onValueChange = onValueChangeFun,
            //modifier = Modifier.padding(bottom = 10.dp),
            //modifier = Modifier.padding(start = if (leadingIconImageVector != null) 0.dp else 12.dp),
            label = { Text(text = labelText, style = Typography.body2) },
            singleLine = true,
            leadingIcon = leadingIcon,
            isError = showError,
            trailingIcon = {
                if (showError && !isPasswordField) Icon(imageVector = Icons.Filled.Error, contentDescription = "Error", tint = Color.LightGray)

                if(isPasswordField){
                    IconButton(onClick = { onVisibilityChange(!isPasswordVisible) }) {
                        Icon(
                            imageVector = if (isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = "Password_Visible"
                            //stringResource(R.string.password_hint )
                        )

                    }
                }
            },
            visualTransformation = when {
                isPasswordField && isPasswordVisible -> VisualTransformation.None
                isPasswordField -> PasswordVisualTransformation()
                else -> VisualTransformation.None
            },
            keyboardOptions = keyboardOptions,
        )
        if(showError) {

        }
    }

}

@Composable
fun CustomTextArea(
    value: String,
    onValueChangeFun: (String) -> Unit,
    labelText: String,
    showError: Boolean = false,
    errorMessage: String = ""
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChangeFun,
            label = { Text(text = labelText, style = Typography.body2) },
            isError = showError,
            modifier = Modifier
                .height(100.dp)
        )
    }

}

