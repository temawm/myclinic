package com.example.myclinic

import CatalogScreen
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import com.example.myclinic.navigation.AppNavGraph
import com.example.myclinic.screens.HomeScreenChilds.DoctorScreen
import com.example.myclinic.screens.HomeScreenChilds.HomeScreen
import com.example.myclinic.screens.LoginScreen
import com.google.firebase.FirebaseApp


class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppNavGraph()
        }
    }
}
