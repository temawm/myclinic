package com.example.myclinic.screens.HomeScreenChilds

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myclinic.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogScreen(){
    var search by remember { mutableStateOf("") }
    Column(modifier = Modifier
        .fillMaxSize()
        .background(Color.White)
        .padding(top = 30.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "Выбор специалиста", modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth(),
            fontSize = 34.sp,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier
            .height(30.dp))
        TextField(
            value = search,
            onValueChange = {
                search = it
            },
            label = { Text("Поиск...", color = Color.Gray) },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 12.dp, end = 12.dp)
                .border(
                    1.dp,
                    colorResource(R.color.authorization_mark),
                    RoundedCornerShape(15.dp)
                ),
            placeholder = { Text(text = "Оториноларинголог", color = Color.Gray) },
            colors = TextFieldDefaults.textFieldColors
                (
                containerColor = Color.White,
                cursorColor = colorResource(id = R.color.authorization_mark),
                focusedIndicatorColor = Color.White,
                unfocusedIndicatorColor = Color.White,

                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black
            )
        )
        Spacer(modifier = Modifier.height(30.dp))
        Spacer(modifier = Modifier
            .height(2.dp)
            .fillMaxWidth()
            .background(Color.LightGray))

    }



}