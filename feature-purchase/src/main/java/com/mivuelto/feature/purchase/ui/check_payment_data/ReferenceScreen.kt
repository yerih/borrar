package com.mivuelto.feature.purchase.ui.check_payment_data

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.mivuelto.core.ui.SingleFormScreen
import com.mivuelto.feature.purchase.ui.navigation.CheckPaymentViewModel

@Composable
fun ReferenceScreen(
    viewModel: CheckPaymentViewModel,
    onBack: ()->Unit = {},
    onTaskDone: ()->Unit = {}
){
    var reference by remember{ mutableStateOf("") }

    SingleFormScreen(
        title = "Ingrese referencia",
        label = "Referencia",
        onBack = onBack,
        onValueChange = { reference = it },
        onTaskDone = {
            viewModel.state.reference = reference
            onTaskDone()
        }
    )
}