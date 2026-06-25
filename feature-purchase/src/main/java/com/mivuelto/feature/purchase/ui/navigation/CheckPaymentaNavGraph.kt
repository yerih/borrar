package com.mivuelto.feature.purchase.ui.navigation

import androidx.compose.runtime.rememberCoroutineScope
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import com.mivuelto.core.domain.model.CheckPaymentModel
import com.mivuelto.core.ui.LoaderScreen
import com.mivuelto.core.ui.design.composableWithTransitions
import com.mivuelto.core.ui.NavFeature
import com.mivuelto.core.ui.sharedViewModel
import com.mivuelto.feature.purchase.ui.invoices.InvoiceScreen
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
            val viewModel = it.sharedViewModel<CheckPaymentViewModel>(navController)
            CheckPaymentScreen(
                onBack = { navController.popBackStack() },
                onTaskDone = { data ->
                    viewModel.updateState(data)
                    navController.navigate(CheckPaymentFlow.LOADER.route)
                }
            )
        }

        composableWithTransitions(
            route = CheckPaymentFlow.LOADER.route
        ){
            val viewModel: CheckPaymentViewModel = it.sharedViewModel(navController)
            LoaderScreen(
                eventFlow = viewModel.effect,
                action = { viewModel.sendPayment() },
                onTaskDone = {
                    navController.navigate(CheckPaymentFlow.INVOICE.route)
                }
            )
        }

        composableWithTransitions(
            route = CheckPaymentFlow.INVOICE.route
        ){
            InvoiceScreen(
                onTaskDone = {
                    navController.navigate(NavFeature.HOME.route){
                        popUpTo(NavFeature.LOGIN.route) { inclusive = true }
                    }
                },
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



