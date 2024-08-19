package com.example.myclinic.screens.HomeScreenChilds

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign


@Composable
fun ProfileScreen(){
    Text( text = "Profile",
        modifier = Modifier.fillMaxSize().wrapContentHeight(),
        textAlign = TextAlign.Center
    )
}