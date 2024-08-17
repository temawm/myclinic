package com.example.myclinic.screens.HomeScreenChilds

import com.example.myclinic.R

sealed class BottomHomeScreenNavigation(val title: String, val iconId: Int, val route: String) {
    object CatalogScreen: BottomHomeScreenNavigation("CatalogScreen", R.drawable.catalogicon, "CatalogScreen")
    object CalendarScreen: BottomHomeScreenNavigation("CalendarScreen", R.drawable.calendaricon, "CalendarScreen")
    object ProfileScreen: BottomHomeScreenNavigation("ProfileScreen", R.drawable.profileicon, "ProfileScreen")


}