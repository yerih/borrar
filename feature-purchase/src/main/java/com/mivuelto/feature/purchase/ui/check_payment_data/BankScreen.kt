package com.mivuelto.feature.purchase.ui.check_payment_data

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.mivuelto.core.ui.SingleFormScreen
import com.mivuelto.feature.purchase.ui.navigation.CheckPaymentViewModel


@Composable
fun BankScreen(
    viewModel: CheckPaymentViewModel,
    onBack: ()->Unit,
    onTaskDone: ()->Unit
){

    var bank by remember{ mutableStateOf("") }

    SingleFormScreen(
        title = "Ingrese banco",
        label = "Banco",
        errorMsg = "El banco es requerido",
        onBack = onBack,
        onTaskDone = {
            viewModel.state.bank = bank
            onTaskDone()
        },
        onValueChange = { bank = it }
    )
}