package com.mivuelto.feature.purchase.ui.check_payment_data

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.mivuelto.core.ui.SingleFormScreen
import com.mivuelto.feature.purchase.ui.navigation.CheckPaymentViewModel


@Composable
fun PhoneScreen(
    viewModel: CheckPaymentViewModel,
    onBack: ()->Unit,
    onTaskDone: ()->Unit
){

    var phone by remember{ mutableStateOf("") }

    SingleFormScreen(
        title = "Ingrese teléfono",
        label = "Teléfono",
        onBack = onBack,
        errorMsg = "El teléfono es requerido",
        onTaskDone = {
            viewModel.state.phone = phone
            onTaskDone()
        },
        onValueChange = { phone = it }
    )
}