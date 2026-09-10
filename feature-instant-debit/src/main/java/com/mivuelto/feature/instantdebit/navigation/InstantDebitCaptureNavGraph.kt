package com.mivuelto.feature.instantdebit.navigation

import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import com.mivuelto.core.ui.NavFeature
import com.mivuelto.core.ui.design.composableWithTransitions
import com.mivuelto.core.ui.sharedViewModel
import com.mivuelto.feature.instantdebit.R
import com.mivuelto.feature.instantdebit.ui.AmountScreen
import com.mivuelto.feature.instantdebit.ui.BankScreen
import com.mivuelto.feature.instantdebit.ui.PhoneScreen


sealed class InstantDebitCaptureFlow(val route: String) {
    object AMOUNT : InstantDebitCaptureFlow("instant_debit_capture/amount")
    object PHONE : InstantDebitCaptureFlow("instant_debit_capture/phone")
    object BANK : InstantDebitCaptureFlow("instant_debit_capture/bank")
}


fun NavGraphBuilder.instantDebitCaptureNavGraph(
    navController: NavController
) {
    navigation(
        startDestination = InstantDebitCaptureFlow.AMOUNT.route,
        route = InstantDebitFlow.FORM.route
    ){

        composableWithTransitions(
            route = InstantDebitCaptureFlow.AMOUNT.route
        ){
            val viewModel = it.sharedViewModel<InstantDebitViewModel>(navController, route = NavFeature.INSTANT_DEBIT.route)
            AmountScreen(
                viewModel = viewModel,
                flowTitle = stringResource(R.string.instant_debit),
                onBack = { navController.popBackStack(route = NavFeature.HOME.route, inclusive = false) },
                onTaskDone = { navController.navigate(route = InstantDebitCaptureFlow.PHONE.route) }
            )
        }
        composableWithTransitions(
            route = InstantDebitCaptureFlow.PHONE.route
        ){
            val viewModel = it.sharedViewModel<InstantDebitViewModel>(navController, route = NavFeature.INSTANT_DEBIT.route)
            PhoneScreen(
                viewModel = viewModel,
                flowTitle = stringResource(R.string.instant_debit),
                onBack = { navController.popBackStack(route = NavFeature.HOME.route, inclusive = false) },
                onTaskDone = { navController.navigate(route = InstantDebitCaptureFlow.BANK.route) }
            )
        }
        composableWithTransitions(
            route = InstantDebitCaptureFlow.BANK.route
        ){
            val viewModel = it.sharedViewModel<InstantDebitViewModel>(navController, route = NavFeature.INSTANT_DEBIT.route)
            BankScreen(
                viewModel = viewModel,
                flowTitle = stringResource(R.string.instant_debit),
                onBack = { navController.popBackStack(route = NavFeature.HOME.route, inclusive = false) },
                onTaskDone = { navController.navigate(route = InstantDebitFlow.LOADER.route) }
            )
        }

    }
}
