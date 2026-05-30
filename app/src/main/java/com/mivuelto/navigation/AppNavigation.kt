package com.mivuelto.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mivuelto.core.ui.NavFeature
import com.mivuelto.feature.purchase.ChangeScreen
import com.mivuelto.feature.purchase.HistoricalScreen
import com.mivuelto.feature.purchase.HomeScreen
import com.mivuelto.feature.purchase.LoginScreen
import com.mivuelto.feature.purchase.SettingScreen
import com.mivuelto.feature.purchase.check_payment.CheckPaymentScreen
import com.mivuelto.feature.purchase.check_payment.checkPaymentGraph

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = NavFeature.LOGIN.route
    ) {
        composable(NavFeature.LOGIN.route) {
            LoginScreen(
                onLoginClick = { usuario, contraseña ->
                    navController.navigate(NavFeature.HOME.route)
                },
                onBack = {
                    navController.popBackStack()
                }
            )
        }
        composable(NavFeature.HOME.route) {
            HomeScreen(
                onFunctionClicked = {
                    navController.navigate(it.route)
                },
                onBack = { navController.popBackStack() }
            )
        }
        checkPaymentGraph(navController)
        composable(NavFeature.CHANGE.route) {
            ChangeScreen(
                onBack = { navController.popBackStack() }
            )
        }
        composable(NavFeature.HISTORICAL.route) {
            HistoricalScreen(
                onBack = { navController.popBackStack() }
            )
        }
        composable(NavFeature.SETTING.route) {
            SettingScreen(
                onBack = { navController.popBackStack() }
            )
        }
    }
}
