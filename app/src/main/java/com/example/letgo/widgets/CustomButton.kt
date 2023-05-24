package com.example.letgo.widgets



import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun CustomButton(
    btnText: String,
    onClickFun: () -> Unit,
    btnColor: Color
) {
    Button(
        onClick = onClickFun,
        shape = RoundedCornerShape(5.dp),
        colors = ButtonDefaults.buttonColors(backgroundColor = btnColor),
        modifier = Modifier.width(280.dp)
    ) {
        Text(
            text = btnText,
            color = MaterialTheme.colorScheme.background
        )
    }
}

@Composable
fun CustomTextButton(
    btnText: String,
    onClickFun: () -> Unit,
) {
    TextButton(
        onClick = onClickFun,
    ) {
        Text(
            text = btnText,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(10.dp)
        )
    }
}