package com.mivuelto.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mivuelto.core.ui.NavFeature
import com.mivuelto.feature.home.navigation.homeGraph
import com.mivuelto.feature.instantdebit.navigation.instantDebitGraph
import com.mivuelto.feature.purchase.DigitalChangeScreen
import com.mivuelto.feature.purchase.HistoricalScreen
import com.mivuelto.feature.purchase.ui.login.LoginScreen
import com.mivuelto.feature.purchase.SettingScreen
import com.mivuelto.feature.purchase.ui.navigation.checkPaymentGraph

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = NavFeature.LOGIN.route
    ) {
        composable(NavFeature.LOGIN.route) {
            LoginScreen(
                onLoginSuccess = { navController.navigate(NavFeature.HOME.route) },
                onBack = {
                    navController.popBackStack()
                }
            )
        }
        homeGraph(navController)
        checkPaymentGraph(navController)
        instantDebitGraph(navController)
        composable(NavFeature.DIGITAL_CHANGE.route) {
            DigitalChangeScreen(
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
