package com.gideon.tamagotchi.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.gideon.tamagotchi.ui.create.CreatePetScreen
import com.gideon.tamagotchi.ui.gameover.GameOverScreen
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
                HomeScreen(
                    onNoPet = {
                        navController.navigate(Routes.Create) {
                            popUpTo(Routes.Home) { inclusive = true }
                        }
                    },
                    onPetDied = {
                        navController.navigate(Routes.GameOver) {
                            popUpTo(Routes.Home) { inclusive = true }
                        }
                    }
                )
            }
            composable(Routes.Create) {
                CreatePetScreen(
                    onPetCreated = {
                        navController.navigate(Routes.Home) {
                            popUpTo(Routes.Create) { inclusive = true }
                        }
                    }
                )
            }
            composable(Routes.GameOver) {
                GameOverScreen(
                    onNewPet = {
                        navController.navigate(Routes.Create) {
                            popUpTo(Routes.GameOver) { inclusive = true }
                        }
                    }
                )
            }
        }
    }
}
