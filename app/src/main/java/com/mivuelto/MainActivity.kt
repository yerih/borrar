package com.mivuelto

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mivuelto.core.ui.navigation.NavFeature
import com.mivuelto.core.ui.theme.CorpoCreditTheme
import com.mivuelto.feature.purchase.PurchaseScreen
import com.mivuelto.feature.purchase.WelcomeScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CorpoCreditTheme {
                MainNavigation()
            }
        }
    }
}

@Composable
fun MainNavigation() {
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
