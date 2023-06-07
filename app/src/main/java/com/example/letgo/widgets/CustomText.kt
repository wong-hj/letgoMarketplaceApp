package com.example.letgo.widgets

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.letgo.ui.theme.Typography

@Composable
fun CustomHeader(
    value: String
){
    Column(
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .padding(top = 40.dp, start = 40.dp, bottom = 20.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = value,
            color = Color.Black,
            style = Typography.h1
        )
    }

}

@Composable
fun CustomSmallSection(
    header: String,
    value: String
){
    Text(
        text = header,
        color = Color.Black,
        style = Typography.subtitle1
    )

    Text(
        text = value,
        color = Color.DarkGray,
        style = Typography.subtitle2
    )
}

@Composable
fun CustomIconText(
    value: String,
    icon: ImageVector? = null
) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 10.dp, bottom = 10.dp)) {
        if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = "Icon",
                tint = Color.Black,
                modifier = Modifier.size(24.dp)
            )
        }
        Text(
            text = value,
            color = Color.Black,
            style = Typography.subtitle1,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}

