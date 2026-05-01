package com.mivuelto.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mivuelto.core.ui.navigation.NavFeature
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
                    navController.navigate(NavFeature.PURCHASE.route)
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
