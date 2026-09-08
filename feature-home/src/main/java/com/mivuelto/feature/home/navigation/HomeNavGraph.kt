package com.mivuelto.feature.home.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.mivuelto.core.ui.NavFeature
import com.mivuelto.feature.home.HomeScreen

fun NavGraphBuilder.homeGraph(navController: NavController) {
    composable(NavFeature.HOME.route) {
        HomeScreen(
            onFunctionClicked = { navController.navigate(it.route) },
            onBack = { navController.popBackStack() }
        )
    }
}
