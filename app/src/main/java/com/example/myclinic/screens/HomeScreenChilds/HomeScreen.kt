package com.example.myclinic.screens.HomeScreenChilds

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.myclinic.R
import org.w3c.dom.Text

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun HomeScreen() {
    val navControllerForHomeScreen = rememberNavController()
    Scaffold(
        bottomBar = {
            BottomNavigation(navControllerForHomeScreen)
        }
    ) {
        NavGraphForHomeScreen(navHostController = navControllerForHomeScreen)
    }
}

@Composable
fun BottomNavigation(navController: NavController) {
    val listBottomNavigationItems = listOf(
        BottomHomeScreenNavigation.CatalogScreen,
        BottomHomeScreenNavigation.CalendarScreen,
        BottomHomeScreenNavigation.ProfileScreen
    )
    androidx.compose.material.BottomNavigation(
        backgroundColor = Color.White,
    ) {
        val backStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = backStackEntry?.destination?.route

        listBottomNavigationItems.forEach { item ->
            BottomNavigationItem(
                selected = currentRoute == item.route,
                onClick = {
                    Log.d("HomeScreen.kt", "navController started")
                    val route = if (item.route == BottomHomeScreenNavigation.CalendarScreen.route) {
                        "CalendarScreen/${""}"
                    } else {
                        item.route
                    }
                    navController.navigate(route) {
                        launchSingleTop = true
                    }
        },
        icon = {
            Icon(painter = painterResource(id = item.iconId), contentDescription = "Icon")
        },
        label = {
            Text(
                text = item.title,
                color = colorResource(id = R.color.authorization_mark),
                fontSize = 9.sp
            )
        },
        selectedContentColor = colorResource(id = R.color.authorization_mark),
        unselectedContentColor = Color.Gray
        )
    }

}
}