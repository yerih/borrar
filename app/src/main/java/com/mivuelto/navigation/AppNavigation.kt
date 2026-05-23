package com.mivuelto.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mivuelto.core.ui.navigation.NavFeature
import com.mivuelto.feature.purchase.HomeScreen
import com.mivuelto.feature.purchase.LoginScreen
import com.mivuelto.feature.purchase.PurchaseScreen
import com.mivuelto.feature.purchase.WelcomeScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = NavFeature.WELCOME.route
    ) {
        composable(NavFeature.WELCOME.route) {
            WelcomeScreen(
                onNavigateToPurchase = {
                    navController.navigate(NavFeature.LOGIN.route) {
                        popUpTo(NavFeature.WELCOME.route) { inclusive = true }
                    }
                }
            )
        }
        composable(NavFeature.LOGIN.route) {
            LoginScreen(
                onLoginClick = { usuario, contraseña ->
                    navController.navigate(NavFeature.HOME.route) {
                        popUpTo(NavFeature.LOGIN.route) { inclusive = true }
                    }
                },
                onBack = {
                    navController.navigate(NavFeature.WELCOME.route) {
                        popUpTo(NavFeature.LOGIN.route) { inclusive = true }
                    }
                }
            )
        }
        composable(NavFeature.HOME.route) {
            HomeScreen(
                onBack = {
                    navController.navigate(NavFeature.LOGIN.route) {
                        popUpTo(NavFeature.HOME.route) { inclusive = true }
                    }
                }
            )
        }
        composable(NavFeature.PURCHASE.route) {
            PurchaseScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
