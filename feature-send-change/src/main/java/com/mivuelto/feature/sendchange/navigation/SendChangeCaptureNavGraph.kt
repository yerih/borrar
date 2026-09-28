package com.mivuelto.feature.sendchange.navigation

import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import com.mivuelto.core.ui.NavFeature
import com.mivuelto.core.ui.R
import com.mivuelto.core.ui.design.composableWithTransitions
import com.mivuelto.core.ui.sharedViewModel
import com.mivuelto.feature.sendchange.ui.AmountScreen
import com.mivuelto.feature.sendchange.ui.BankScreen
import com.mivuelto.feature.sendchange.ui.IdScreen
import com.mivuelto.feature.sendchange.ui.PhoneScreen


sealed class SendChangeCaptureFlow(val route: String) {
    object ID : SendChangeCaptureFlow("send_change_capture/id")
    object AMOUNT : SendChangeCaptureFlow("send_change_capture/amount")
    object PHONE : SendChangeCaptureFlow("send_change_capture/phone")
    object BANK : SendChangeCaptureFlow("send_change_capture/bank")
}


fun NavGraphBuilder.sendChangeCaptureNavGraph(
    navController: NavController
) {
    navigation(
        startDestination = SendChangeCaptureFlow.ID.route,
        route = SendChangeFlow.FORM.route
    ){

        composableWithTransitions(
            route = SendChangeCaptureFlow.ID.route
        ){
            val viewModel = it.sharedViewModel<SendChangeViewModel>(navController, route = NavFeature.SEND_CHANGE.route)
            IdScreen(
                viewModel = viewModel,
                flowTitle = stringResource(com.mivuelto.feature.sendchange.R.string.send_change),
                onBack = { navController.popBackStack(route = NavFeature.HOME.route, inclusive = false) },
                onTaskDone = { navController.navigate(route = SendChangeCaptureFlow.AMOUNT.route) }
            )
        }
        composableWithTransitions(
            route = SendChangeCaptureFlow.AMOUNT.route
        ){
            val viewModel = it.sharedViewModel<SendChangeViewModel>(navController, route = NavFeature.SEND_CHANGE.route)
            AmountScreen(
                viewModel = viewModel,
                flowTitle = stringResource(com.mivuelto.feature.sendchange.R.string.send_change),
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
                flowTitle = stringResource(com.mivuelto.feature.sendchange.R.string.send_change),
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
                flowTitle = stringResource(com.mivuelto.feature.sendchange.R.string.send_change),
                onBack = { navController.popBackStack(route = NavFeature.HOME.route, inclusive = false) },
                onTaskDone = { navController.navigate(route = SendChangeFlow.LOADER.route) }
            )
        }

    }
}
