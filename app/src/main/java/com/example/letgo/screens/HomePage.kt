package com.example.letgo.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Search
//import androidx.compose.material.Scaffold
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.letgo.nav.Routes
import com.example.letgo.widgets.CustomBottomBar
import com.example.letgo.widgets.CustomHeader
import com.example.letgo.widgets.CustomTopBar

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun HomePage(navController: NavHostController) {


    var searchText by remember { mutableStateOf("") }
    var active by remember { mutableStateOf(false) }
    var items = remember {
        mutableStateListOf(
            "a", "s"
        )
    }

    val cardItems = listOf("Hello", "Hoho", "Hello", "Hoho", "Hello", "Hoho", "Hello", "Hoho", "Hello", "Hoho", "Hello", "Hoho")


    Scaffold(

        content = {
            Column(modifier = Modifier.fillMaxWidth()) {
                CustomHeader(value = "Discover")

                SearchBar(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally),
                    query = searchText,
                    onQueryChange = {
                        searchText = it
                    },
                    onSearch = {
                        active = false
                    },
                    active = active,
                    onActiveChange = {
                        active = it
                    },
                    placeholder = {
                        Text(text = "Search")
                    },
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
                    },
                    trailingIcon = {
                        if(active) {
                            Icon(
                                modifier = Modifier.clickable {
                                    if(searchText.isNotEmpty()) {
                                        searchText = ""
                                    } else {
                                        active = false
                                    }

                                },
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close"
                            )
                        }

                    }
                ) {
                    items.forEach {
                        Row(modifier = Modifier.padding(all = 14.dp)) {
                            Icon(
                                modifier = Modifier.padding(end = 10.dp),
                                imageVector = Icons.Default.History,
                                contentDescription = "History"
                            )

                            Text(text = it)

                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.align(Alignment.CenterHorizontally).padding(start = 20.dp, end = 20.dp, bottom = 70.dp),
                ) {
                    items(cardItems.size) { cardItem ->
                        Card(
                            onClick = { /* Do something */ },
                            modifier = Modifier
                                .size(width = 180.dp, height = 200.dp)
                                .align(Alignment.CenterHorizontally)
                        ) {
                            Box(Modifier.fillMaxSize()) {
                                Text("Clickable", Modifier.align(Alignment.Center))
                            }
                        }
                    }
                }


            }


        },
        bottomBar = {
            CustomBottomBar(navController = navController)
        }
    )

        //}

    //)


}