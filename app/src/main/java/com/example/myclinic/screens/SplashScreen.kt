package com.example.myclinic.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.myclinic.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavHostController) {
    LaunchedEffect(Unit) {
        delay(2000)
        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        if (currentUser != null && currentUser.isEmailVerified) {
            navController.navigate("HomeScreen") {
                popUpTo("SplashScreen") { inclusive = true }
                Log.d("Splash", "$currentUser, ${currentUser.isEmailVerified}")
            }
        } else {
            navController.navigate("LoginScreen") {
                popUpTo("SplashScreen") { inclusive = true }
                if (currentUser != null) {
                    Log.d("Splash", "$currentUser, ${currentUser.isEmailVerified}")
                }

            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.mark),
            contentDescription = "authorization_logo",
            modifier = Modifier
                .size(128.dp)
                .clip(RoundedCornerShape(16.dp))
        )
        Text(
            text = "Iclinic",
            modifier = Modifier
                .wrapContentWidth()
                .wrapContentHeight(),
            fontSize = 60.sp,
            color = colorResource(id = R.color.authorization_mark)
        )
    }
}
