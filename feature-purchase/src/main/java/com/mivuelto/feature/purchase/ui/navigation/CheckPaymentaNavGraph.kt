package com.mivuelto.feature.purchase.ui.navigation

import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import com.mivuelto.core.ui.LoaderScreen
import com.mivuelto.core.ui.design.composableWithTransitions
import com.mivuelto.core.ui.NavFeature
import com.mivuelto.feature.purchase.InvoiceScreen
import com.mivuelto.feature.purchase.ui.check_payment.CheckPaymentScreen
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


sealed class CheckPaymentFlow(val route: String) {

    object FORM : CheckPaymentFlow("check_payment/start")

    object INVOICE : CheckPaymentFlow("check_payment/invoice")
    object LOADER : CheckPaymentFlow("check_payment/loader")

    object ERROR : CheckPaymentFlow("check_payment/confirm")

}

fun NavGraphBuilder.checkPaymentGraph(navController: NavController) {
    navigation(
        startDestination = CheckPaymentFlow.FORM.route,
        route = NavFeature.CHECK_PAYMENT.route
    ) {
        composableWithTransitions(
            route = CheckPaymentFlow.FORM.route,
        ){
            CheckPaymentScreen(
                onBack = { navController.popBackStack() },
                onTaskDone = { _, _ ->
                    navController.navigate(CheckPaymentFlow.LOADER.route)
                }
            )
        }

        composableWithTransitions(
            route = CheckPaymentFlow.LOADER.route
        ){
            val scope = rememberCoroutineScope()
            LoaderScreen(
                action = {
                    scope.launch {
                        delay(3000)
                        navController.navigate(CheckPaymentFlow.INVOICE.route)
                    }
                }
            )
        }

        composableWithTransitions(
            route = CheckPaymentFlow.INVOICE.route
        ){
            InvoiceScreen(
                onBack = { navController.navigate(NavFeature.HOME.route){
                    popUpTo(NavFeature.LOGIN.route) { inclusive = true }
                } }
            )
        }

        composableWithTransitions(
            route = CheckPaymentFlow.ERROR.route
        ){

        }
    }
}



