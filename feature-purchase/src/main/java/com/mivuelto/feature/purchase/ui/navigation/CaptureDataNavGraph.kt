package com.mivuelto.feature.purchase.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import com.mivuelto.core.ui.NavFeature
import com.mivuelto.core.ui.design.composableWithTransitions
import com.mivuelto.core.ui.sharedViewModel
import com.mivuelto.feature.purchase.ui.check_payment_data.AmountScreen
import com.mivuelto.feature.purchase.ui.check_payment_data.ReferenceScreen


sealed class CaptureDataFlow(val route: String) {
    object REFERENCE : CaptureDataFlow("capture_data/reference")
    object AMOUNT : CaptureDataFlow("capture_data/amount")
    object PHONE : CaptureDataFlow("capture_data/phone")
    object BANK : CaptureDataFlow("capture_data/bank")
}


fun NavGraphBuilder.captureDataNavGraph(navController: NavController) {
    navigation(
        startDestination = CaptureDataFlow.REFERENCE.route,
        route = CheckPaymentFlow.FORM.route
    ){

        composableWithTransitions(
            route = CaptureDataFlow.REFERENCE.route
        ){
            val viewModel = it.sharedViewModel<CheckPaymentViewModel>(navController)
            ReferenceScreen(
                onBack = navController::popBackStack,
                onTaskDone = { navController.navigate(route = CaptureDataFlow.AMOUNT.route) },
                viewModel = viewModel
            )
        }

        composableWithTransitions(
            route = CaptureDataFlow.AMOUNT.route
        ){
            val viewModel = it.sharedViewModel<CheckPaymentViewModel>(navController)
            AmountScreen(
                viewModel = viewModel,
                onBack = navController::popBackStack,
                onTaskDone = { navController.popBackStack(route = NavFeature.HOME.route, inclusive = false) }
            )
        }

    }
}