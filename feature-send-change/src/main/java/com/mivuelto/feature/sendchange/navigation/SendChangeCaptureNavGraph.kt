package com.mivuelto.feature.sendchange.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import com.mivuelto.core.ui.NavFeature
import com.mivuelto.core.ui.design.composableWithTransitions
import com.mivuelto.core.ui.sharedViewModel
import com.mivuelto.feature.sendchange.ui.AmountScreen
import com.mivuelto.feature.sendchange.ui.BankScreen
import com.mivuelto.feature.sendchange.ui.PhoneScreen


sealed class SendChangeCaptureFlow(val route: String) {
    object AMOUNT : SendChangeCaptureFlow("send_change_capture/amount")
    object PHONE : SendChangeCaptureFlow("send_change_capture/phone")
    object BANK : SendChangeCaptureFlow("send_change_capture/bank")
}


fun NavGraphBuilder.sendChangeCaptureNavGraph(
    navController: NavController
) {
    navigation(
        startDestination = SendChangeCaptureFlow.AMOUNT.route,
        route = SendChangeFlow.FORM.route
    ){

        composableWithTransitions(
            route = SendChangeCaptureFlow.AMOUNT.route
        ){
            val viewModel = it.sharedViewModel<SendChangeViewModel>(navController, route = NavFeature.SEND_CHANGE.route)
            AmountScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack(route = NavFeature.HOME.route, inclusive = false) },
                onTaskDone = { navController.navigate(route = SendChangeCaptureFlow.PHONE.route) }
            )
        }
        composableWithTransitions(
            route = SendChangeCaptureFlow.PHONE.route
        ){
            val viewModel = it.sharedViewModel<SendChangeViewModel>(navController, route = NavFeature.SEND_CHANGE.route)
            PhoneScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack(route = NavFeature.HOME.route, inclusive = false) },
                onTaskDone = { navController.navigate(route = SendChangeCaptureFlow.BANK.route) }
            )
        }
        composableWithTransitions(
            route = SendChangeCaptureFlow.BANK.route
        ){
            val viewModel = it.sharedViewModel<SendChangeViewModel>(navController, route = NavFeature.SEND_CHANGE.route)
            BankScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack(route = NavFeature.HOME.route, inclusive = false) },
                onTaskDone = { navController.navigate(route = SendChangeFlow.LOADER.route) }
            )
        }

    }
}
