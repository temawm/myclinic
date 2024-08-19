package com.example.myclinic.screens.HomeScreenChilds

import CatalogScreen
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun NavGraphForHomeScreen(navHostController: NavHostController) {
    NavHost(navHostController, startDestination = "CatalogScreen") {
        composable("HomeScreen") {
            HomeScreen()
        }
        composable("ProfileScreen") {
            ProfileScreen()
        }
        composable("CatalogScreen") {
            CatalogScreen()
        }
        composable("CalendarScreen") {
            CalendarScreen()
        }
    }
}
