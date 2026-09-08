package com.mivuelto.feature.instantdebit.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import com.mivuelto.core.ui.LoaderScreen
import com.mivuelto.core.ui.design.composableWithTransitions
import com.mivuelto.core.ui.NavFeature
import com.mivuelto.core.ui.sharedViewModel
import com.mivuelto.feature.instantdebit.ui.invoices.InvoiceScreen


sealed class InstantDebitFlow(val route: String) {

    object FORM : InstantDebitFlow("instant_debit/start")

    object INVOICE : InstantDebitFlow("instant_debit/invoice")
    object LOADER : InstantDebitFlow("instant_debit/loader")

    object ERROR : InstantDebitFlow("instant_debit/confirm")

}

fun NavGraphBuilder.instantDebitGraph(navController: NavController) {
    navigation(
        startDestination = InstantDebitFlow.FORM.route,
        route = NavFeature.INSTANT_DEBIT.route
    ) {

        instantDebitCaptureNavGraph(navController)
        composableWithTransitions(
            route = InstantDebitFlow.LOADER.route
        ){
            val viewModel: InstantDebitViewModel = it.sharedViewModel(navController)
            LoaderScreen(
                eventFlow = viewModel.effect,
                action = { viewModel.sendPayment() },
                onTaskDone = {
                    navController.navigate(InstantDebitFlow.INVOICE.route)
                }
            )
        }

        composableWithTransitions(
            route = InstantDebitFlow.INVOICE.route
        ){
            val viewModel: InstantDebitViewModel = it.sharedViewModel(navController)
            InvoiceScreen(
                data = viewModel.getInvoice(),
                onTaskDone = { navController.popBackStack(route = NavFeature.HOME.route, inclusive = false) },
                onBack = { navController.popBackStack(route = NavFeature.HOME.route, inclusive = false) }
            )
        }

        composableWithTransitions(
            route = InstantDebitFlow.ERROR.route
        ){

        }
    }
}

