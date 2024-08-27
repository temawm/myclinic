package com.example.myclinic.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myclinic.screens.HomeScreenChilds.HomeScreen
import com.example.myclinic.screens.HomeScreenChilds.NavGraphForHomeScreen
import com.example.myclinic.screens.LoginScreen
import com.example.myclinic.screens.SplashScreen


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavGraph() {
    val navController = rememberNavController()

    NavHost(navController, startDestination = "SplashScreen") {
        composable("LoginScreen") {
                LoginScreen(navController = navController)
        }
        composable( "HomeScreen"){
                HomeScreen()
        }
        composable("SplashScreen") {
            SplashScreen(navController = navController)
        }
    }

}


