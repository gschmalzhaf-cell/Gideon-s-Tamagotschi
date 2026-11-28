package com.gideon.tamagotchi.ui

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.gideon.tamagotchi.ui.create.CreatePetScreen
import com.gideon.tamagotchi.ui.gameover.GameOverScreen
import com.gideon.tamagotchi.ui.home.HomeScreen
import com.gideon.tamagotchi.ui.navigation.Routes
import com.gideon.tamagotchi.ui.settings.SettingsScreen
import com.gideon.tamagotchi.ui.theme.GideonTheme

@Composable
fun GideonApp() {
    GideonTheme(dynamicColor = true) {
        val navController = rememberNavController()
        NavHost(
            navController = navController,
            startDestination = Routes.Home,
            enterTransition = { fadeIn(tween(260)) + scaleIn(initialScale = 0.96f, animationSpec = tween(260)) },
            exitTransition = { fadeOut(tween(200)) + scaleOut(targetScale = 0.98f, animationSpec = tween(200)) },
            popEnterTransition = { fadeIn(tween(240)) + scaleIn(initialScale = 0.98f, animationSpec = tween(240)) },
            popExitTransition = { fadeOut(tween(200)) + scaleOut(targetScale = 0.96f, animationSpec = tween(200)) }
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
                    },
                    onOpenSettings = {
                        navController.navigate(Routes.Settings)
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
            composable(Routes.Settings) {
                SettingsScreen(
                    onNavigateUp = { navController.popBackStack() }
                )
            }
        }
    }
}
