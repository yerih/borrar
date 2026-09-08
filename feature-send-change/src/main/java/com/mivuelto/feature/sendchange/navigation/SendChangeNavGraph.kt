package com.mivuelto.feature.sendchange.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import com.mivuelto.core.ui.LoaderScreen
import com.mivuelto.core.ui.design.composableWithTransitions
import com.mivuelto.core.ui.NavFeature
import com.mivuelto.core.ui.sharedViewModel
import com.mivuelto.feature.sendchange.ui.invoices.InvoiceScreen


sealed class SendChangeFlow(val route: String) {

    object FORM : SendChangeFlow("send_change/start")

    object INVOICE : SendChangeFlow("send_change/invoice")
    object LOADER : SendChangeFlow("send_change/loader")

    object ERROR : SendChangeFlow("send_change/confirm")

}

fun NavGraphBuilder.sendChangeGraph(navController: NavController) {
    navigation(
        startDestination = SendChangeFlow.FORM.route,
        route = NavFeature.SEND_CHANGE.route
    ) {

        sendChangeCaptureNavGraph(navController)
        composableWithTransitions(
            route = SendChangeFlow.LOADER.route
        ){
            val viewModel: SendChangeViewModel = it.sharedViewModel(navController)
            LoaderScreen(
                eventFlow = viewModel.effect,
                action = { viewModel.sendPayment() },
                onTaskDone = {
                    navController.navigate(SendChangeFlow.INVOICE.route)
                }
            )
        }

        composableWithTransitions(
            route = SendChangeFlow.INVOICE.route
        ){
            val viewModel: SendChangeViewModel = it.sharedViewModel(navController)
            InvoiceScreen(
                data = viewModel.getInvoice(),
                onTaskDone = { navController.popBackStack(route = NavFeature.HOME.route, inclusive = false) },
                onBack = { navController.popBackStack(route = NavFeature.HOME.route, inclusive = false) }
            )
        }

        composableWithTransitions(
            route = SendChangeFlow.ERROR.route
        ){

        }
    }
}
