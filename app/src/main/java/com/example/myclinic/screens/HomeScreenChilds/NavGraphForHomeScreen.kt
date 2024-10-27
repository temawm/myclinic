package com.example.myclinic.screens.HomeScreenChilds

import CatalogScreen
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable


@RequiresApi(Build.VERSION_CODES.O)
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
            val catalogViewModel: CatalogViewModel = hiltViewModel()
            CatalogScreen(navHostController = navHostController, CatalogViewModel = catalogViewModel)
        }
        composable("CalendarScreen/{doctorName}") { backStackEntry ->
            val doctorName = backStackEntry.arguments?.getString("doctorName")
            doctorName?.let {
                CalendarScreen(doctorName = it, navController = navHostController)
            }
        }
        composable("DoctorScreen/{specialization}") { backStackEntry ->
            val specialization = backStackEntry.arguments?.getString("specialization")
            specialization?.let {
                DoctorScreen(specialization = it, navHostController)
            }
        }
    }
}
