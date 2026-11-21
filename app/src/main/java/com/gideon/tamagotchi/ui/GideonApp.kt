package com.gideon.tamagotchi.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.gideon.tamagotchi.ui.home.HomeScreen
import com.gideon.tamagotchi.ui.navigation.Routes
import com.gideon.tamagotchi.ui.theme.GideonTheme

@Composable
fun GideonApp() {
    GideonTheme {
        val navController = rememberNavController()
        NavHost(
            navController = navController,
            startDestination = Routes.Home
        ) {
            composable(Routes.Home) {
                HomeScreen()
            }
        }
    }
}
