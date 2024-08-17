package com.example.myclinic.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myclinic.screens.HomeScreenChilds.HomeScreen
import com.example.myclinic.screens.HomeScreenChilds.NavGraphForHomeScreen
import com.example.myclinic.screens.LoginScreen


@Composable
fun AppNavGraph() {
    val navController = rememberNavController()

    NavHost(navController, startDestination = "LoginScreen") {
        composable("LoginScreen") {
                LoginScreen(navController = navController)
        }
        composable( "HomeScreen"){
                HomeScreen()
        }
    }

}


