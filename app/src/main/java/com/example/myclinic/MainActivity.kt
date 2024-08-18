package com.example.myclinic

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.myclinic.navigation.AppNavGraph
import com.example.myclinic.screens.HomeScreenChilds.CatalogScreen


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
                CatalogScreen()
        }
    }
}
