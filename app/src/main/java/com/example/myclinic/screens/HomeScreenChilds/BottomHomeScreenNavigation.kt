package com.example.myclinic.screens.HomeScreenChilds

import com.example.myclinic.R

sealed class BottomHomeScreenNavigation(val title: String, val iconId: Int, val route: String) {
    object CatalogScreen: BottomHomeScreenNavigation("Каталог", R.drawable.catalogicon, "CatalogScreen")
    object CalendarScreen: BottomHomeScreenNavigation("Календарь", R.drawable.calendaricon, "CalendarScreen")
    object ProfileScreen: BottomHomeScreenNavigation("Профиль", R.drawable.profileicon, "ProfileScreen")
}