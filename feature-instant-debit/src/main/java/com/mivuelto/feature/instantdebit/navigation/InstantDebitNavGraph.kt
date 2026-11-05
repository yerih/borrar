package com.mivuelto.feature.instantdebit.navigation

import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import com.mivuelto.core.ui.ErrorScreen
import com.mivuelto.core.ui.LoaderScreen
import com.mivuelto.core.ui.design.composableWithTransitions
import com.mivuelto.core.ui.NavFeature
import com.mivuelto.core.ui.sharedViewModel
import com.mivuelto.feature.instantdebit.R
import com.mivuelto.feature.instantdebit.ui.TokenScreen
import com.mivuelto.feature.instantdebit.ui.invoices.InvoiceScreen


sealed class InstantDebitFlow(val route: String) {

    object FORM : InstantDebitFlow("instant_debit/start")

    object INVOICE : InstantDebitFlow("instant_debit/invoice")
    object CHARGE_LOADER : InstantDebitFlow("instant_debit/charge_loader")
    object TOKEN : InstantDebitFlow("instant_debit/token")

    object ERROR : InstantDebitFlow("instant_debit/confirm")

}

fun NavGraphBuilder.instantDebitGraph(navController: NavController) {
    navigation(
        startDestination = InstantDebitFlow.FORM.route,
        route = NavFeature.INSTANT_DEBIT.route
    ) {

        instantDebitCaptureNavGraph(navController)

        composableWithTransitions(
            route = InstantDebitFlow.TOKEN.route
        ){
            val viewModel: InstantDebitViewModel = it.sharedViewModel(navController)
            TokenScreen(
                viewModel = viewModel,
                flowTitle = stringResource(R.string.instant_debit),
                onBack = { navController.popBackStack(route = NavFeature.HOME.route, inclusive = false) },
                onCharge = { navController.navigate(InstantDebitFlow.CHARGE_LOADER.route) }
            )
        }

        composableWithTransitions(
            route = InstantDebitFlow.CHARGE_LOADER.route
        ){
            val viewModel: InstantDebitViewModel = it.sharedViewModel(navController)
            LoaderScreen(
                eventFlow = viewModel.effect,
                action = { viewModel.chargePayment() },
                onTaskDone = {
                    navController.navigate(InstantDebitFlow.INVOICE.route)
                },
                onError = { navController.navigate(InstantDebitFlow.ERROR.route) }
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
            val viewModel: InstantDebitViewModel = it.sharedViewModel(navController)
            ErrorScreen(
                text = viewModel.errorMsg,
                withRetryBtn = false,
                onBack = { navController.popBackStack(route = NavFeature.HOME.route, inclusive = false) }
            )
        }
    }
}

