package com.mivuelto.feature.purchase.ui.check_payment_data

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavController
import com.mivuelto.core.ui.SingleFormScreen
import com.mivuelto.feature.purchase.ui.navigation.CheckPaymentViewModel

@Composable
fun AmountScreen(
    viewModel: CheckPaymentViewModel,
    onBack: ()->Unit,
    onTaskDone: ()->Unit
){
    var amount by remember{ mutableStateOf("") }

    SingleFormScreen(
        title = "Ingrese monto",
        label = "Monto",
        onBack = onBack,
        onTaskDone = {
            viewModel.state.amount = amount
            onTaskDone()
        },
        onValueChange = { amount = it }
    )
}