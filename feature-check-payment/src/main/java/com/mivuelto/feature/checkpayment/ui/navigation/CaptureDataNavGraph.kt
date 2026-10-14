package com.mivuelto.feature.checkpayment.ui.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import com.mediosdepago.corpocredit.core.ui_atomics.UiEvent
import com.mivuelto.core.ui.NavFeature
import com.mivuelto.core.ui.design.composableWithTransitions
import com.mivuelto.core.ui.sharedViewModel
import com.mivuelto.feature.checkpayment.R
import com.mivuelto.feature.checkpayment.ui.check_payment_data.AmountScreen
import com.mivuelto.feature.checkpayment.ui.check_payment_data.BankScreen
import com.mivuelto.feature.checkpayment.ui.check_payment_data.DateScreen
import com.mivuelto.feature.checkpayment.ui.check_payment_data.PhoneScreen
import com.mivuelto.feature.checkpayment.ui.check_payment_data.ReferenceScreen


sealed class CaptureDataFlow(val route: String) {
    object DATE : CaptureDataFlow("capture_data/date")
    object REFERENCE : CaptureDataFlow("capture_data/reference")
    object AMOUNT : CaptureDataFlow("capture_data/amount")
    object PHONE : CaptureDataFlow("capture_data/phone")
    object BANK : CaptureDataFlow("capture_data/bank")
}


fun NavGraphBuilder.captureDataNavGraph(
    navController: NavController
) {
    navigation(
//        startDestination = CaptureDataFlow.BANK.route,
        startDestination = CaptureDataFlow.DATE.route,
        route = CheckPaymentFlow.FORM.route
    ){

        composableWithTransitions(
            route = CaptureDataFlow.DATE.route
        ){
            val viewModel = it.sharedViewModel<CheckPaymentViewModel>(navController, route = NavFeature.CHECK_PAYMENT.route)
            DateScreen(
                onBack = { navController.popBackStack(route = NavFeature.HOME.route, inclusive = false) },
                onTaskDone = { navController.navigate(route = CaptureDataFlow.REFERENCE.route) },
                viewModel = viewModel,
                flowTitle = stringResource(R.string.verify_payment_mobile)
            )
        }

        composableWithTransitions(
            route = CaptureDataFlow.REFERENCE.route
        ){
            val viewModel = it.sharedViewModel<CheckPaymentViewModel>(navController, route = NavFeature.CHECK_PAYMENT.route)
            ReferenceScreen(
                onBack = { navController.popBackStack(route = NavFeature.HOME.route, inclusive = false) },
                onTaskDone = { navController.navigate(route = CaptureDataFlow.AMOUNT.route) },
                viewModel = viewModel,
                flowTitle = stringResource(R.string.verify_payment_mobile)
            )
        }

        composableWithTransitions(
            route = CaptureDataFlow.AMOUNT.route
        ){
            val viewModel = it.sharedViewModel<CheckPaymentViewModel>(navController, route = NavFeature.CHECK_PAYMENT.route)
            AmountScreen(
                flowTitle = stringResource(R.string.verify_payment_mobile),
                viewModel = viewModel,
                onBack = { navController.popBackStack(route = NavFeature.HOME.route, inclusive = false) },
                onTaskDone = { navController.navigate(route = CaptureDataFlow.PHONE.route) }
            )
        }
        composableWithTransitions(
            route = CaptureDataFlow.PHONE.route
        ){
            val viewModel = it.sharedViewModel<CheckPaymentViewModel>(navController, route = NavFeature.CHECK_PAYMENT.route)
            PhoneScreen(
                flowTitle = stringResource(R.string.verify_payment_mobile),
                viewModel = viewModel,
                onBack = { navController.popBackStack(route = NavFeature.HOME.route, inclusive = false) },
                onTaskDone = { navController.navigate(route = CaptureDataFlow.BANK.route) }
            )
        }
        composableWithTransitions(
            route = CaptureDataFlow.BANK.route
        ){
            val viewModel = it.sharedViewModel<CheckPaymentViewModel>(navController, route = NavFeature.CHECK_PAYMENT.route)
            LaunchedEffect(Unit) {
                viewModel.effect.collect { event ->
                    if (event is UiEvent.Error) {
                        navController.navigate(CheckPaymentFlow.ERROR.route)
                    }
                }
            }
            BankScreen(
                flowTitle = stringResource(R.string.verify_payment_mobile),
                viewModel = viewModel,
                onBack = { navController.popBackStack(route = NavFeature.HOME.route, inclusive = false) },
                onTaskDone = { navController.navigate(route = CheckPaymentFlow.LOADER.route) }
            )
        }

    }
}